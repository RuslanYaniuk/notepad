package com.mynote.controllers;

import com.google.common.collect.Lists;
import com.mynote.dto.CsrfTokenDTO;
import com.mynote.models.UserRole;
import com.mynote.utils.CustomSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController extends AbstractController {

    public static final String XSRF_TOKEN_NAME = "XSRF-TOKEN";

    @Autowired
    private CustomSessionContext customSessionContext;

    @RequestMapping(value = "/get-token", method = GET)
    public ResponseEntity getToken(HttpServletRequest httpServletRequest) throws IOException {
        CsrfToken csrfToken = (CsrfToken) httpServletRequest.getAttribute(CsrfToken.class.getName());

        return ok(new CsrfTokenDTO(csrfToken));
    }

    @RequestMapping(value = "/get-authorities", method = GET)
    public ResponseEntity getAuthorities(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = customSessionContext.getAuthentication();

        setCsrfCookies(httpServletRequest, httpServletResponse);

        if (authentication != null && authentication.isAuthenticated()) {
            List<UserRole> roles = Lists.newArrayList();

            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                roles.add(new UserRole(grantedAuthority.getAuthority()));
            }
            return ok(roles);
        }
        return messageNotFound("user.authentication.status.notLoggedIn");
    }

    public static void setCsrfCookies(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        CsrfToken csrfToken = (CsrfToken) httpServletRequest.getAttribute(CsrfToken.class.getName());
        Cookie cookie = new Cookie(XSRF_TOKEN_NAME, csrfToken.getToken());

        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
}
