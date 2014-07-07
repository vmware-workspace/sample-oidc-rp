package com.vmware.horizon.oidc.rp.model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.openidconnect.IdToken;

public class OIDCCredentials {
    private String idTokenString;
    private Credential credential;
    private IdToken idToken;

    public OIDCCredentials(String idTokenString, Credential credential, IdToken idToken) {
        this.idTokenString = idTokenString;
        this.credential = credential;
        this.idToken = idToken;
    }

    public IdToken getIdToken() {
        return idToken;
    }

    public String getIdTokenString() {
        return idTokenString;
    }

    public Credential getCredential() {
        return credential;
    }
}
