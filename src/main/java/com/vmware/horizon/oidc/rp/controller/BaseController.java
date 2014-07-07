package com.vmware.horizon.oidc.rp.controller;

import com.vmware.horizon.oidc.rp.model.OIDCCredentials;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class BaseController {

    protected String getUserNameFromSession(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("userName");
    }

    protected OIDCCredentials getCredentialsFromSession(HttpServletRequest request) {
        return (OIDCCredentials) request.getSession().getAttribute("credentials");
    }

    protected void removeSessiongCookie(HttpServletResponse response) {
        Cookie c = new Cookie("oidc", "");
        c.setMaxAge(0);
        c.setPath("/");
        response.addCookie(c);

    }

    protected void saveSessionData(HttpServletRequest request, HttpServletResponse response, OIDCCredentials oidcCredentials) {
        issueSessionCookie(response);
        HttpSession session = request.getSession();
        session.setAttribute("credentials", oidcCredentials);
        session.setAttribute("userName", oidcCredentials.getIdToken().getPayload().getSubject());
    }


    protected void issueSessionCookie(HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("oidc", uuid);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
