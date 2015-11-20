package com.mynote.controllers;

import com.mynote.config.web.Constants;
import com.mynote.dto.user.UserDTO;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import com.mynote.utils.UserRoleDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.mynote.config.security.CustomAuthenticationSuccessHandler.SESSION_ATTRIBUTE_USER_ID;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping(value = "/api/user/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get-user-info", method = GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity getUserInfo(HttpSession httpSession) throws IOException, UserNotFoundException {
        Long userId = (Long) httpSession.getAttribute(SESSION_ATTRIBUTE_USER_ID);
        User user = userService.findUserById(userId);
        UserDTO userDTO = new UserDTO(user);

        if (user.getRoles().contains(Constants.ROLE_ADMIN)) {
            userDTO.setUserRoleDTOs(UserRoleDtoUtil.convert(user.getRoles()));
        }

        return new ResponseEntity<>(userDTO, OK);
    }
}
