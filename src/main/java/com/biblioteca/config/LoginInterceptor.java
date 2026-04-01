package com.biblioteca.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final String SESSION_LOGIN = "BIBLIOTECA_LOGIN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith(request.getContextPath() + "/css")
                || uri.startsWith(request.getContextPath() + "/images") 
                || uri.startsWith(request.getContextPath() + "/webjars")
                || uri.endsWith(".ico")
                || uri.contains("/error")) {
            return true;
        }
        String path = uri.substring(request.getContextPath().length());
        if (path.equals("/login") || path.startsWith("/login?")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute(SESSION_LOGIN))) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
