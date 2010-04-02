/* Copyright, 2009 Bani (http://baniverso.com) & Chester (http://chester.blog.br)
 * 
 * This file is part of MemeThis.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.memethis.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import com.memethis.data.Bookmarklet;
import com.memethis.data.PMF;
import com.memethis.util.ApplicationConfig;
import com.memethis.util.HashHelper;
import com.simpleyql.Api;
import com.simpleyql.ApiFactory;
import com.simpleyql.QueryResult;

/**
 * @author Bani
 * @author Chester
 */
public class MemeServlet extends HttpServlet {

	private static final long serialVersionUID = 9029402675900981932L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Parameters from bookmarklet (have to get them manually because
		// request.getParameter f#@%s with parameters generated with
		// JavaScript's encode() function).
		if (request.getQueryString() == null) {
			error(
					request,
					response,
					"Your request was missing parameters. If this error persists, we suggest you reinstall the bookmarklet.");
			return;
		}
		String[] parts = request.getQueryString().split("&");
		String id = null, contentURL = null, imageURL = null, hash = null, caption = null;
		boolean isLink = false;
		for (String part : parts) {
			String[] keyvalue = part.split("=");
			String key = keyvalue[0];
			String value = (keyvalue.length > 1 ? keyvalue[1] : "");
			// GAE re-encodes the querystring using utf-8 (bug?)
			if (useUTF8(request)) {
				value = URLDecoder.decode(value, "utf-8");
			}
			// Some sites pre-insert "%uNNNN" encoding, we have to deal with it
			value = replacePercentU(value);
			// This is the real encode (was iso-8859-1, but the jQuery jsonp
			// plugin does utf-8). No relationship with the GAE encoding above.
			value = URLDecoder.decode(value, "utf-8");
			if (key.equals("id")) {
				id = value;
			} else if (key.equals("content_url")) {
				contentURL = value;
			} else if (key.equals("image_url")) {
				imageURL = value;
			} else if (key.equals("hash")) {
				hash = value;
			} else if (key.equals("caption")) {
				caption = value;
			} else if (key.equals("is_link")) {
				if ("1".equals(value))
					isLink = true;
			}
		}
		if (id == null || id.equals("") || id.equals("null") || contentURL == null || hash == null) {
			error(
					request,
					response,
					"Your request was missing parameters. If this error persists, we suggest you reinstall the bookmarklet.");
			return;
		}
		if (caption == null)
			caption = "";
		if (imageURL == null)
			imageURL = "";
		contentURL = contentURL.replace(' ', '+'); // JavaScript escape()
		// artifact :-(
		if (contentURL.matches("^http://(get.)?memethis.com/[a-z].*")) {
			log("Self meme");
			error(request, response,
					"Look for something else to send to meme. This page won't work");
			return;
		}

		// Retrieve the bookmarklet's DB info and check hash
		Bookmarklet b;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		b = pm.getObjectById(Bookmarklet.class, new Long(id));
		if (!hash.equals(HashHelper.hash(b.getToken() + contentURL + imageURL))) {
			log("invalid hash: got [" + hash + "], expected ["
					+ HashHelper.hash(b.getToken() + contentURL + imageURL)
					+ "] for [" + b.getToken() + contentURL + imageURL + "]");
			error(
					request,
					response,
					"We were unable to verify the authenticity of your request.<br/>If you think your request was legitimate, please try again later.<br/>Otherwise, keep testing and let us know is you find any bugs ;-)");
			return;
		}

		String yql = generateYQL(contentURL, caption, imageURL, isLink,
				ApplicationConfig.getURL());

		Api api = ApiFactory.getApiInstance(ApplicationConfig.getKey(),
				ApplicationConfig.getSecret(), ApplicationConfig.getURL()
						+ "/simpleyqlcallback", false, null);
		QueryResult qr = null;
		try {
			qr = api.query(yql, b.getAuthdata());
		} catch (OAuthException e) {
			log("Error posting to Y!:", e);
			error(request, response,
					"Internal error while posting to Yahoo! Meme");
			return;
		} catch (RuntimeException e) {
			if (e.getMessage().equals("Unable to complete the HTTP request")) {
				if (e.getCause().getMessage()
						.contains("Timeout while fetching")) {
					log("Unable to complete Yahoo! query", e);
				} else {
					log("Error posting to Y!:", e);
					error(request, response,
							"Internal error while posting to Yahoo! Meme");
					return;
				}
			} else {
				throw e;
			}
		}
		if (!qr.getText().contains("<message>ok</message>")) {
			log("Yahoo! Meme did not return ok message. Return:" + qr.getText());
			error(request, response,
					"Yahoo! Meme did not return <message>ok</message>");
			return;
		}

		// Update token, if needed (for future requests)
		if (qr != null && !qr.getAuthdata().equals(b.getAuthdata())) {
			b.setAuthdata(qr.getAuthdata());
			try {
				pm.makePersistent(b);
			} finally {
				pm.close();
			}
		}

		sendResponseAsJsonpData(request, response, "OK");

	}

	/**
	 * Replaces "%uNNNN" encodings with the real unicode characters
	 * 
	 * @param value
	 *            String which may contain such encodings
	 * @return String with the corresponding characters
	 */
	private String replacePercentU(String value) {
		int pos;
		while ((pos = value.indexOf("%u")) != -1) {
			value = value.substring(0, pos)
					+ (char) (Integer.valueOf(
							value.substring(pos + 2, pos + 6), 16).intValue())
					+ value.substring(pos + 6);
		}
		return value;
	}

	/**
	 * Checks if we are on the Google AppEngine system (in a really lousy way)
	 * because GAE re-encodes the querystring using utf-8 (bug?)
	 * 
	 * @param request
	 *            this servlet's request object
	 * @return true if we are on GAE, false if not
	 */
	private boolean useUTF8(HttpServletRequest request) {
		return request.getRequestURL().toString().contains("memethis.com");
	}

	/**
	 * Analyzes the URL to check what kind of content should be sent and where
	 * to add HTML link tags
	 * 
	 * @param contentURL
	 *            the url the user was visiting when the bookmarklet was used
	 * @param caption
	 *            the text the user added to the div
	 * @param imageURL
	 *            url of the image to be sent
	 * @param isLink
	 *            create a link with the caption if true
	 * @param ourURL
	 *            the base url of our application
	 * @return the YQL string that should be used
	 */
	private String generateYQL(String contentURL, String caption,
			String imageURL, boolean isLink, String ourURL) {
		// Decide what to publish based on the URL
		String lc = contentURL.toLowerCase();
		String type;
		if (lc.endsWith(".jpg") || lc.endsWith(".jpeg") || lc.endsWith(".gif")
				|| lc.endsWith(".png") || !imageURL.equals("")) {
			type = "photo";
		} else if (lc.startsWith("http://www.youtube.com")
				|| lc.startsWith("http://www.vimeo.com")
				|| lc.startsWith("http://vimeo.com")) {
			type = "video";
		} else if (lc.endsWith(".mp3")) {
			type = "audio";
		} else {
			type = "text";
		}
		
		if (caption.equals("")) {
			if(isLink)
				caption = contentURL;
		}

		// If the user inserted links on his caption, (s)he knows what (s)he's doing, leave it alone
		if (!caption.toLowerCase().contains("<a")) {
			boolean hasViaMemeThis = caption.endsWith("(via MemeThis)"); 
			if (hasViaMemeThis) {
				caption=caption.substring(0,caption.length()-14);
			}
			if (isLink) {
				caption = "<a href=\"" + contentURL + "\">" + caption + "</a>";
			}
			if (hasViaMemeThis) {
				caption += "(via <a href=\"" + ourURL
						+ "\">MemeThis</a>)";	
			}
		}

		if (!imageURL.equals("")) {
			contentURL = imageURL;
		}
				

		// Publish it!
		String yql;
		contentURL = contentURL.replaceAll("'", "\\\\'");
		caption = caption.replaceAll("'", "\\\\'");
		if (type.equals("text")) {
			yql = "INSERT INTO meme.user.posts (type, content) VALUES ('"
					+ type + "', '" + caption + "');";
		} else {
			yql = "INSERT INTO meme.user.posts (type, content, caption) VALUES ('"
					+ type + "', '" + contentURL + "',  '" + caption + "');";
		}

		return yql;
	}

	private void error(HttpServletRequest request,
			HttpServletResponse response, String msg) throws ServletException,
			IOException {
		response.setStatus(500);
		sendResponseAsJsonpData(request, response, msg);
	}

	private void sendResponseAsJsonpData(HttpServletRequest request,
			HttpServletResponse response, String msg) throws ServletException,
			IOException {
		response.getWriter().write(
				request.getParameter("callback") + "({msg: '"
						+ msg.replaceAll("'", "`") + "'})");
	}

}
