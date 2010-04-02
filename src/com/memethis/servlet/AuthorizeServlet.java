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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import com.memethis.util.ApplicationConfig;
import com.simpleyql.Api;
import com.simpleyql.ApiFactory;

/**
 * @author Bani
 * @author Chester
 */
public class AuthorizeServlet extends HttpServlet {

	private static final long serialVersionUID = -6337734084853438615L;

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Api api = ApiFactory.getApiInstance(ApplicationConfig.getKey(),
				ApplicationConfig.getSecret(), ApplicationConfig.getURL()
						+ "/simpleyqlcallback", false, null);

		try {
			api.askAuthorization(request, response, ApplicationConfig.getURL()
					+ "/generate");
		} catch (OAuthException e) {
			response.getWriter().println(
					"An internal error happened (OAuthException)");
			log("An internal error happened (OAuthException)", e);
		}

	}
}
