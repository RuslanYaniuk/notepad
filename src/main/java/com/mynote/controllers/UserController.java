package com.mynote.controllers;

import com.mynote.dto.user.UserInfoDTO;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.mynote.config.security.CustomAuthenticationSuccessHandler.SESSION_ATTRIBUTE_USER_ID;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping(value = "/api/user/")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get-info", method = GET)
    public ResponseEntity getInfo(HttpSession httpSession) throws IOException, UserNotFoundException {
        Long userId = (Long) httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID);
        User user = userService.findUserById(userId);
        UserInfoDTO userDTO = new UserInfoDTO(user);

        return ok(userDTO);
    }
}
