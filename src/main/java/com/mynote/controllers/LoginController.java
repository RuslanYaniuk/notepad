package com.mynote.controllers;

import com.mynote.config.web.Constants;
import com.mynote.config.web.ExtendedMessageSource;
import com.mynote.dto.CsrfTokenDTO;
import com.mynote.dto.MessageDTO;
import com.mynote.dto.user.UserRoleDTO;
import com.mynote.utils.UserRoleDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@RestController
@RequestMapping(value = "/api/login")
public class LoginController {

    public static final String XSRF_TOKEN = "XSRF-TOKEN";

    @Autowired
    private ExtendedMessageSource messageSource;

    @RequestMapping(value = "/get-token", method = GET, produces = "application/json;charset=UTF-8")
    public CsrfTokenDTO getToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        CsrfToken csrfToken = applyCsrfCookies(httpServletRequest, httpServletResponse);

        return new CsrfTokenDTO(csrfToken);
    }

    @RequestMapping(value = "/get-authorities", method = GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity getAuthorities(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserRoleDTO[] roleDTOs;
        MessageDTO errorMessage;
        Collection authorities;

        applyCsrfCookies(httpServletRequest, httpServletResponse);

        if (authentication != null && authentication.isAuthenticated()) {
            authorities = authentication.getAuthorities();

            if (authorities.contains(Constants.ROLE_ADMIN) ||
                    authorities.contains(Constants.ROLE_USER) ||
                    authorities.contains(Constants.ROLE_ANONYMOUS)) {
                roleDTOs = UserRoleDtoUtil.convertAuthorities(authentication.getAuthorities());

                return new ResponseEntity<>(roleDTOs, OK);
            }
        }

        errorMessage = messageSource.getMessageDTO("user.authentication.status.notLoggedIn",
                LocaleContextHolder.getLocale());

        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }

    public static CsrfToken applyCsrfCookies(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        CsrfToken csrfToken = (CsrfToken) httpServletRequest.getAttribute(CsrfToken.class.getName());
        Cookie cookie = new Cookie(XSRF_TOKEN, csrfToken.getToken());

        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return csrfToken;
    }
}
