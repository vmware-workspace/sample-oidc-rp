package com.vmware.horizon.oidc.rp.interceptor;

import com.vmware.horizon.oidc.rp.controller.AuthenticatedController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component(value = "sessionManagementInterceptor")
public class SessionManagementInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object controller = ((HandlerMethod) handler).getBean();
        String sessionUuid = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if ("oidc".equals(cookies[i].getName())) {
                    sessionUuid = cookies[i].getValue();
                }
            }
        }
        if (controller instanceof AuthenticatedController) {
            if (StringUtils.isBlank(sessionUuid)) {
                response.sendRedirect("/");
                return false;
            } else {
                if (request.getSession().getAttribute("credentials") == null) {
                    response.sendRedirect("/");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handlerMethod, ModelAndView modelAndView) throws Exception {
        int a =1;
        ; // nothing to do
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handlerMethod, Exception e) throws Exception {
        ; // nothing to do
    }
}

