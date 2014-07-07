package com.vmware.horizon.oidc.rp.controller;

import com.google.api.services.plus.model.Person;
import com.vmware.horizon.oidc.rp.model.OIDCCredentials;
import com.vmware.horizon.oidc.rp.service.OIDCClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class TokenController extends AuthenticatedController {

    @Value("${oauth2.auth.userinfo.url}")
    private String userInfoEndpoint;

    @Value("${oauth2.rs.api.url}")
    private String apiEndpoint;

    @Autowired
    private OIDCClientService oidcClientService;

    @RequestMapping("/verify")
    public String verifyAccessToken(@RequestParam(required = true) String token, @RequestParam(required = true) String type, ModelMap model) throws IOException {
        String response = oidcClientService.verifyToken(token, type);
        model.put("response", response);
        model.put("tokenType", type);
        return "token";
    }

    @RequestMapping("/launch")
    public String verifyAccessToken(ModelMap model, HttpServletRequest request) throws IOException {
        OIDCCredentials oidcCredentials = getCredentialsFromSession(request);
        Person data = oidcClientService.callSPApiWithCredentials(oidcCredentials.getCredential(), apiEndpoint);
        model.put("title", "Result of google plus api launch");
        model.put("data", data);
        return "launch";
    }

    @RequestMapping("/userinfo")
    private String authCodeFlowToGetUserInfo(ModelMap model, HttpServletRequest request) throws IOException {
        OIDCCredentials oidcCredentials = getCredentialsFromSession(request);
        String data =  oidcClientService.callUserInfoEndpoint(oidcCredentials.getCredential(), userInfoEndpoint).toString();
        model.put("title", "Result from google openid userinfo endpoint");
        model.put("data", data);
        return "userinfo";
    }
}
