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
package com.memethis.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;
import com.memethis.util.HashHelper;

/**
 * Represents each bookmarklet generated for an user.
 * 
 * @author bani
 * @author chester
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Bookmarklet {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	/**
	 * Used to authenticate the bookmarklet
	 */
	@Persistent
	private String token;

	/**
	 * Allows interaction with Y! on behalf of this user
	 */
	@Persistent
	private Text authdata;
	
	/**
	 * Version of MemeThis that the user is running 
	 */
	@Persistent
	private Integer version = 6;

	public Bookmarklet(String authdata) {
		this.authdata = new Text(authdata);
		this.token = HashHelper.sha1(Double.toString(Math.random()));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAuthdata() {
		return authdata.getValue();
	}

	public void setAuthdata(String authdata) {
		this.authdata = new Text(authdata);
	}

}
