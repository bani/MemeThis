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

import com.memethis.data.Bookmarklet;
import com.memethis.data.PMF;
import com.memethis.util.ApplicationConfig;

/**
 * @author Bani
 * @author Chester
 */
public class GenerateServlet extends HttpServlet {

	private static final long serialVersionUID = -9068676695369147987L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Bookmarklet b = new Bookmarklet(URLDecoder.decode(request
				.getParameter("authdata"), "US-ASCII"));
		try {
			pm.makePersistent(b);
		} finally {
			pm.close();
		}
		request.setAttribute("bookmarklet", generateJs(b));
		getServletConfig().getServletContext().getRequestDispatcher(
				"/showbookmarklet.jsp").forward(request, response);

	}

	private String generateJs(Bookmarklet b) throws IOException {

		String memeThisData = b.getId() + "|" + b.getToken();
		return "javascript:(function(){"
				+ "var%20d=document;"
				// Scroll up and create a "loading" box visible at the top
				+ "var%20l=d.createElement('div');l.id='memethis_loading';l.setAttribute('style','position:fixed;top:0;background-color:red;color:black;padding:2px;z-index:1000000001;');l.innerHTML='Loading...';d.body.appendChild(l);"
				// Create an element with the user' ID/token data (will be
				// retrieved by the overlay)
				+ "var%20t=d.createElement('div');t.id='memethis_data';t.setAttribute('style','display:none;');t.innerHTML='"
				+ memeThisData
				+ "';d.body.appendChild(t);"
				// Inject the overlay loading script (which will clear the
				// "loading" box)
				+ "var%20e=d.createElement('script');e.setAttribute('language','javascript');e.setAttribute('src','"
				+ ApplicationConfig.getURL()
				+ "/js/load.js');d.body.appendChild(e);})();";

	}

}
