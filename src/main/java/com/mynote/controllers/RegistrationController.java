package com.mynote.controllers;

import com.mynote.dto.user.UserFindDTO;
import com.mynote.dto.user.UserInfoDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.dto.user.UserSimpleRegistrationDTO;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.exceptions.SearchFieldsAreEmpty;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping(value = "/api/registration/")
public class RegistrationController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    protected ProviderManager providerManager;

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/full", method = PUT)
    public ResponseEntity registerNewUser(@Validated @RequestBody UserRegistrationDTO userRegistrationDTO) throws EmailAlreadyTakenException, LoginAlreadyTakenException, InterruptedException {
        User user = userService.addUser(userRegistrationDTO.getUser());

        return ok(new UserInfoDTO(user));
    }

    @RequestMapping(value = "/simple", method = PUT)
    public ResponseEntity simple(@Validated @RequestBody UserSimpleRegistrationDTO userSimpleRegistrationDTO) throws EmailAlreadyTakenException, LoginAlreadyTakenException, InterruptedException {
        User user = userSimpleRegistrationDTO.getUser();
        Boolean signIn = userSimpleRegistrationDTO.getSignIn() == null ? false : userSimpleRegistrationDTO.getSignIn();
        UsernamePasswordAuthenticationToken token;
        String password = user.getPassword();

        user = userService.addUser(user.getEmail(), password);
        if (signIn) {
            token = new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        return ok(new UserInfoDTO(user));
    }

    @RequestMapping(value = "/check-available", method = POST)
    public ResponseEntity checkAvailable(@Validated @RequestBody UserFindDTO userFindDTO) throws SearchFieldsAreEmpty {
        String login = userFindDTO.getLogin();
        String email = userFindDTO.getEmail();

        if (login != null) {
            try {
                userService.findUserByLogin(login);
                return messageOK("user.login.isNotAvailable");
            } catch (UserNotFoundException e) {
                return messageOK("user.login.isAvailable");
            }
        }
        if (email != null) {
            try {
                userService.findUserByEmail(email);
                return messageOK("user.email.isNotAvailable");
            } catch (UserNotFoundException e) {
                return messageOK("user.email.isAvailable");
            }
        }
        throw new SearchFieldsAreEmpty();
    }
}
