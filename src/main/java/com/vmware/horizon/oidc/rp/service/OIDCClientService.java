package com.vmware.horizon.oidc.rp.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.plus.model.Person;
import com.vmware.horizon.oidc.rp.model.OIDCCredentials;

import java.io.IOException;

public interface OIDCClientService {

    public Person callSPApiWithCredentials(Credential credential, String spUrl) throws IOException;

    public OIDCCredentials performAuthenticationWithAuthCode(String code) throws IOException;

    public String getAuthorizationCodeUrl() throws IOException;

    public String verifyToken(String token, String type) throws IOException;

    String callUserInfoEndpoint(Credential credential, String userInfoUrl) throws IOException;
}
