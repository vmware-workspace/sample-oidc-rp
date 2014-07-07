package com.vmware.horizon.oidc.rp.controller;

import com.vmware.horizon.oidc.rp.service.OIDCClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LogoutController extends BaseController {

    @Value("${oauth2.logout.url}")
    private String authLogoutUrl;

    @Autowired
    private OIDCClientService oidcClientService;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String home(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        removeSessiongCookie(response);
        return "redirect:" + authLogoutUrl;
    }

 }