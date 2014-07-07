package com.vmware.horizon.oidc.rp.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.model.Person;
import com.vmware.horizon.oidc.rp.model.OIDCClientDetails;
import com.vmware.horizon.oidc.rp.model.OIDCCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service(value = "oidcClientService")
public class OIDCClientServiceImpl implements OIDCClientService {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Value("${oauth2.auth.token.url}")
    private String tokenServerUrl;

    @Value("${oauth2.auth.code.url}")
    private String authCodeUrl;

    @Value("${oauth2.token.verification.url}")
    private String tokenVerificationUrl;

    @Autowired
    private OIDCClientDetails oidcClientDetails;

    @Override
    public String getAuthorizationCodeUrl() throws IOException {
        AuthorizationCodeFlow authorizationCodeFlow = buildAutorizationCodeFlowRequest();
        String redirectUri = oidcClientDetails.getRedirectUrl();
        AuthorizationCodeRequestUrl authorizationUrl = authorizationCodeFlow.newAuthorizationUrl().setRedirectUri(redirectUri);
        return authorizationUrl.build();
    }

    @Override
    public String verifyToken(final String token, final String type) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });

        GenericUrl url = new GenericUrl(tokenVerificationUrl);
        HttpRequest request = requestFactory.buildGetRequest(url);
        request.getUrl().set(type, token);
        String response = request.execute().parseAsString();
        return response;

    }

    @Override
    public OIDCCredentials performAuthenticationWithAuthCode(String code) throws IOException {
        AuthorizationCodeFlow authorizationCodeFlow = buildAutorizationCodeFlowRequest();
        IdTokenResponse response = authorizationCodeFlow.newTokenRequest(code).setRedirectUri(oidcClientDetails.getRedirectUrl()).executeUnparsed().parseAs(IdTokenResponse.class);
        Credential credential = authorizationCodeFlow.createAndStoreCredential(response, null);
        IdToken idToken = IdToken.parse(JSON_FACTORY, response.getIdToken());
        return new OIDCCredentials(response.getIdToken(), credential, idToken);
    }

    @Override
    public Person callSPApiWithCredentials(final Credential credential, String spUrl) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                        credential.initialize(request);
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

        GenericUrl url = new GenericUrl(spUrl);
        HttpRequest request = requestFactory.buildGetRequest(url);
        return request.execute().parseAs(Person.class);
    }

    @Override
    public String callUserInfoEndpoint(final Credential credential, String userInfoUrl) throws IOException {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                credential.initialize(request);
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            }
        });
        GenericUrl url = new GenericUrl(userInfoUrl);
        HttpRequest request = requestFactory.buildGetRequest(url);
        return request.execute().parseAsString();
    }

    private AuthorizationCodeFlow buildAutorizationCodeFlowRequest() throws IOException {
        ClientParametersAuthentication clientParametersAuth = new ClientParametersAuthentication(oidcClientDetails.getClientId(), oidcClientDetails.getClientSecret());

        return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(), HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(tokenServerUrl),
                clientParametersAuth, oidcClientDetails.getClientId(), authCodeUrl).setScopes(Arrays.asList(oidcClientDetails.getScope())).build();
    }

}
