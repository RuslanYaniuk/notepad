package com.mynote.config.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static com.mynote.config.Constants.APPLICATION_JSON_UTF8;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private Validator mvcValidator;

    public JsonUsernamePasswordAuthenticationFilter(String route, String httpMethod) {
        super(new AntPathRequestMatcher(route, httpMethod));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        UserLoginDTO user;

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        user = obtainUser(request);

        Assert.notNull(user.getLogin());
        Assert.notNull(user.getPassword());

        authRequest = new UsernamePasswordAuthenticationToken(
                user.getLogin(), user.getPassword());

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    public UserLoginDTO obtainUser(HttpServletRequest request) {
        UserLoginDTO userLoginDTO = getUserLoginDTOFromJsonRequest(request);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(userLoginDTO, "userLoginDTO");

        try {
            mvcValidator.validate(userLoginDTO, bindingResult);
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        if (bindingResult.hasErrors()) {
            throw new AuthenticationServiceException("user.login.validation.error.emptyCredentials");
        }
        return userLoginDTO;
    }

    protected void setDetails(HttpServletRequest request,
                              UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private UserLoginDTO getUserLoginDTOFromJsonRequest(HttpServletRequest request) {
        UserLoginDTO userLoginDTO = null;

        if (!StringUtils.equalsIgnoreCase(APPLICATION_JSON_UTF8, request.getContentType())) { //TODO refactor. should throw message code
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        try (BufferedReader br = request.getReader()) {
            userLoginDTO = jacksonObjectMapper.readValue(br, UserLoginDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userLoginDTO;
    }
}