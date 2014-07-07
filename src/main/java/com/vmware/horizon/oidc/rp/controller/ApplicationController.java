package com.vmware.horizon.oidc.rp.controller;

import com.vmware.horizon.oidc.rp.model.OIDCCredentials;
import com.vmware.horizon.oidc.rp.service.OIDCClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.vmware.horizon.oidc.rp")
@Controller
public class ApplicationController extends BaseController {

    @Autowired
    private OIDCClientService oidcClientService;

    @RequestMapping("/openid")
    public String authCodeFlowToAccessSPApis(@RequestParam(required = false) String code, @RequestParam(required = false) String error,
                                             ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotBlank(error)) {
            return "Error in authorization code flow";
        }

        OIDCCredentials oidcCredentials = getCredentialsFromSession(request);

        if ((oidcCredentials != null && oidcCredentials.getCredential().getExpiresInSeconds() > 60 ) ||  StringUtils.isNotBlank(code)) {
            if (oidcCredentials == null) {
                oidcCredentials = oidcClientService.performAuthenticationWithAuthCode(code);
            }
            saveSessionData(request, response, oidcCredentials);

            model.put("idTokenDecrypted", oidcCredentials.getIdToken().getPayload().toPrettyString());
            model.put("idToken", oidcCredentials.getIdTokenString());
            model.put("accessToken", oidcCredentials.getCredential().getAccessToken());
            return "oidc";
        } else {
            return "redirect:" + oidcClientService.getAuthorizationCodeUrl();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationController.class, args);
    }
}

