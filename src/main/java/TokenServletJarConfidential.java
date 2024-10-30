package main.java;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/get-token-jar-confidential")
public class TokenServletJarConfidential extends HttpServlet {
    private static final Logger logger = Logger.getLogger(TokenServletJarConfidential.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String serverUrl = "http://localhost:8080";
        String realm = "rex-realm";
        String clientId = "rex-client";
        String clientSecret = "QaVrvhOsJpojx0cxuosRut6FO4azq1qt";

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        try {
            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
            String accessToken = tokenResponse.getToken();

            logger.info("Token request successful. Access Token: " + accessToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"access_token\": \"" + accessToken + "\"}");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Token request failed: ", e);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"Token request failed: " + e.getMessage() + "\"}");
        } finally {
            keycloak.close();
        }
    }
}
