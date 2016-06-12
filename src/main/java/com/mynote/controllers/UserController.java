package com.mynote.controllers;

import com.mynote.dto.user.UserInfoDTO;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import com.mynote.utils.CustomSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping(value = "/api/user/")
public class UserController extends AbstractController {

    @Autowired
    private CustomSessionContext customSessionContext;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get-info", method = GET)
    public ResponseEntity getInfo() throws IOException, UserNotFoundException {
        User user = userService.findUserByEmail(customSessionContext.getUser().getUsername());

        return ok(new UserInfoDTO(user));
    }
}
