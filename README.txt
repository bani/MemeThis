MemeThis - http://www.memethis.com/

This project was created by Bani (http://baniverso.com) and Chester (http://chester.blog.br)

You can obtain a copy of the source code at https://sourceforge.net/projects/memethis/

It is licensed under AGPLv3, see LICENSE.txt for more information.

This project was made possible by the use of the simpleyql library, available at http://simpleyql.sourceforge.net/

To run this project, you'll need an API key from Yahoo! that can be obtained from http://developer.yahoo.com/

Once you have that, edit the file appconfig.properties.sample in the src folder with the corresponding information and rename it to appconfig.properties

To deploy the project in AppEngine, you should also edit the file war/WEB-INF/appengine-web.xml by clicking on "App Engine project settings" on the Deploy window.

List of libraries needed (add them to the war/WEB-INF/lib folder):
- appengine-api-1.0-sdk-1.2.6.jar
- appengine-api-labs-1.2.6.jar
- commons-codec-1.2.jar
- commons-httpclient-3.1.jar
- commons-logging-1.0.4.jar
- datanucleus-appengine-1.0.3.jar
- datanucleus-core-1.1.5.jar
- datanucleus-jpa-1.1.5.jar
- geronimo-jpa_3.0_spec-1.1.1.jar
- geronimo-jta_1.1_spec-1.1.1.jar
- jdo2-api-2.3-eb.jar
- simpleyql-0.9.jar
