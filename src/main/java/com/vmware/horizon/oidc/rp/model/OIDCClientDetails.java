package com.vmware.horizon.oidc.rp.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component(value = "oidcClientDetails")
@PropertySource("classpath:config.properties")
public class OIDCClientDetails {

    @Value("${oauth2.client.id}")
    private String clientId;

    @Value("${oauth2.client.secret}")
    private String clientSecret;

    @Value("${oauth2.redirct.uri.host}")
    private String redirectUrlHost;

    @Value("${oauth2.redirct.uri.port}")
    private int redirectUrlPort;

    @Value("${oauth2.redirct.uri.callback.path}")
    private String redirectUrlPath;

    @Value("${oauth2.auth.scope}")
    private String scope;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUrlHost() {
        return redirectUrlHost;
    }

    public void setRedirectUrlHost(String redirectUrlHost) {
        this.redirectUrlHost = redirectUrlHost;
    }

    public int getRedirectUrlPort() {
        return redirectUrlPort;
    }

    public void setRedirectUrlPort(int redirectUrlPort) {
        this.redirectUrlPort = redirectUrlPort;
    }

    public String getRedirectUrlPath() {
        return redirectUrlPath;
    }

    public void setRedirectUrlPath(String redirectUrlPath) {
        this.redirectUrlPath = redirectUrlPath;
    }

    public String getRedirectUrl() {
        return "http://" + redirectUrlHost + ":" + redirectUrlPort + redirectUrlPath;
    }
}
