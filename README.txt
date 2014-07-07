OpenID Connect RP implementation

This project runs a relying party server in tomcat using Java and Spring Boot.

--- Configuration ---
Add your client id, client secret in config.properties. Also change the server url if you are not using Google as OP or RS
oauth2.client.id=Enter your client ID
oauth2.client.secret=Enter your client Secret

--- Running the application ---
To start the server, run the main class ApplicationController.java
Running the below command will compile and run tomcat server on localhost:8080

mvn spring-boot:run

When the server is started -
Go to localhost:8080 in your browser to go to the landing page
Go to localhost:8080/openid - this endpoint performs the authorization code flow


Author: Shraddha Ladda