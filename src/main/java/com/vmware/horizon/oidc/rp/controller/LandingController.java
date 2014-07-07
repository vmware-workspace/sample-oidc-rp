package com.vmware.horizon.oidc.rp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LandingController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(ModelMap model, HttpServletRequest request) {
        return "home";
    }

}
