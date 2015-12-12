package com.mynote.controllers;

import com.mynote.dto.user.UserInfoDTO;
import com.mynote.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.mynote.utils.UserSessionUtil.getCurrentUser;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping(value = "/api/user/")
public class UserController extends AbstractController {

    @RequestMapping(value = "/get-info", method = GET)
    public ResponseEntity getInfo() throws IOException, UserNotFoundException {
        return ok(new UserInfoDTO(getCurrentUser()));
    }
}
