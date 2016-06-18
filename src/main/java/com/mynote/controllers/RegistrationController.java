package com.mynote.controllers;

import com.mynote.dto.user.UserFindDTO;
import com.mynote.dto.user.UserInfoDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.exceptions.SearchFieldsAreEmpty;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/register-new-user", method = PUT)
    public ResponseEntity registerNewUser(@Validated @RequestBody UserRegistrationDTO userRegistrationDTO) throws EmailAlreadyTakenException, LoginAlreadyTakenException, InterruptedException {
        User user = userService.addUser(userRegistrationDTO.getUser());

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
