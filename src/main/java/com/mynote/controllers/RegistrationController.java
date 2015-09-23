package com.mynote.controllers;

import com.mynote.config.ApplicationProperties;
import com.mynote.config.ExtendedMessageSource;
import com.mynote.dto.MessageDTO;
import com.mynote.dto.UserFindDTO;
import com.mynote.dto.UserRegistrationDTO;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.exceptions.SearchFieldsAreEmpty;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping(value = "/api/registration/")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExtendedMessageSource messageSource;

    @Autowired
    private ApplicationProperties appProperties;

    @RequestMapping(value = "/register-new-user", method = PUT, produces = "application/json;charset=UTF-8")
    public ResponseEntity<MessageDTO> registerNewUser(@Validated @RequestBody UserRegistrationDTO userRegistrationDTO,
                                                      Locale locale) throws EmailAlreadyTakenException, LoginAlreadyTakenException, InterruptedException {
        if (appProperties.isBruteForceProtectionEnabled()) {
            Thread.sleep(appProperties.getBruteForceDelay());
        }
        userService.addNewUser(userRegistrationDTO);

        return new ResponseEntity<>(messageSource.getMessageDTO("user.registration.success", locale), OK);
    }

    @RequestMapping(value = "/check-available-email", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity checkAvailableEmail(@RequestBody UserFindDTO userFindDTO, Locale locale) throws SearchFieldsAreEmpty {
        String email = userFindDTO.getEmail();
        MessageDTO messageDTO;

        if (email != null) {
            try {
                userService.findUserByEmail(email);
                messageDTO = messageSource.getMessageDTO("user.email.isNotAvailable", locale);
            } catch (UserNotFoundException e) {
                messageDTO = messageSource.getMessageDTO("user.email.isAvailable", locale);
            }
            return new ResponseEntity<>(messageDTO, OK);
        }

        throw new SearchFieldsAreEmpty();
    }

    @RequestMapping(value = "/check-available-login", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity checkAvailableLogin(@RequestBody UserFindDTO userFindDTO, Locale locale) throws SearchFieldsAreEmpty {
        String login = userFindDTO.getLogin();
        MessageDTO messageDTO;

        if (login != null) {
            try {
                userService.findByLoginOrEmail(login, login);
                messageDTO = messageSource.getMessageDTO("user.login.isNotAvailable", locale);
            } catch (UserNotFoundException e) {
                messageDTO = messageSource.getMessageDTO("user.login.isAvailable", locale);
            }
            return new ResponseEntity<>(messageDTO, OK);
        }

        throw new SearchFieldsAreEmpty();
    }
}
