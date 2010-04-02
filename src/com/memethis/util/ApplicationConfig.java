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
package com.memethis.util;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Bani
 * @author Chester
 */
public class ApplicationConfig {

	// We've tried to implement this class as a singleton but we had trouble
	// using the classloader
	private static Properties properties = null;

	/**
	 * Loads the properties from the file only if the properties attribute is null
	 * 
	 * @return properties from the appconfig.properties file
	 * @throws IOException
	 */
	public synchronized Properties getProperties() throws IOException {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(this.getClass().getClassLoader()
						.getResourceAsStream("appconfig.properties"));
				// test if the properties were loaded correctly, otherwise set
				// it back to null to try again on next call
				properties.getProperty("apikey.consumerkey");
			} catch (IOException e) {
				properties = null;
				throw e;
			} catch (RuntimeException e) {
				properties = null;
				throw e;
			}
		}

		return properties;
	}
	
	public static String getURL() throws IOException {
		if(properties == null)
			(new ApplicationConfig()).getProperties();
		return properties.getProperty("application.baseurl");
	}
	
	public static String getKey() throws IOException {
		if(properties == null)
			(new ApplicationConfig()).getProperties();
		return properties.getProperty("apikey.consumerkey");
	}
	
	public static String getSecret() throws IOException {
		if(properties == null)
			(new ApplicationConfig()).getProperties();
		return properties.getProperty("apikey.consumersecret");
	}

}
