package com.mynote.controllers;

import com.mynote.config.ExtendedMessageSource;
import com.mynote.dto.*;
import com.mynote.exceptions.OperationNotPermitted;
import com.mynote.exceptions.SearchFieldsAreEmpty;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.exceptions.UserRoleNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserRoleService;
import com.mynote.services.UserService;
import com.mynote.utils.UserDtoUtil;
import com.mynote.utils.UserRoleDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping("/api/administration")
public class AdministrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ExtendedMessageSource messageSource;

    @RequestMapping(value = "/list-all-users", method = GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<UserDTO[]> listAllUsers() throws IOException {
        List<User> userList = userService.getAllUsersSortByLastNameDesc();

        return new ResponseEntity<>(UserDtoUtil.convert(userList), OK);
    }

    @RequestMapping(value = "/get-all-user-roles", method = GET, produces = "application/json;charset=UTF-8")
    public UserRoleDTO[] getAllUserRoles() throws IOException {
        return UserRoleDtoUtil.convert(userRoleService.getAllUserRoles());
    }

    @RequestMapping(value = "/update-user", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity updateUser(@Validated @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException, UserRoleNotFoundException {
        return new ResponseEntity<>(new UserUpdateDTO(userService.updateUser(userUpdateDTO)), OK);
    }

    @RequestMapping(value = "/delete-user", method = DELETE, produces = "application/json;charset=UTF-8")
    public ResponseEntity deleteUser(@Validated @RequestBody UserDeleteDTO userDeleteDTO, Locale locale) throws UserNotFoundException, OperationNotPermitted {
        userService.deleteUser(userDeleteDTO);

        return new ResponseEntity<>(messageSource.getMessageDTO("user.account.deleted", locale), OK);
    }

    @RequestMapping(value = "/find-user", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity findUser(@RequestBody UserFindDTO userFindDTO) throws UserNotFoundException, SearchFieldsAreEmpty {
        Long userId = userFindDTO.getId();

        if (userFindDTO.getId() != null) {
            UserDTO userDTO = UserDtoUtil.convert(userService.findUserById(userId));

            return new ResponseEntity<>(userDTO, OK);
        }

        throw new SearchFieldsAreEmpty();
    }

    @RequestMapping(value = "/reset-user-password", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity resetUserPassword(@Validated @RequestBody UserResetPasswordDTO resetPasswordDTO, Locale locale) throws UserNotFoundException {
        User user = userService.resetUserPassword(resetPasswordDTO);

        return new ResponseEntity<>(messageSource.getMessageDTO("user.reset.password.success",
                locale, user.getId().toString()), OK);
    }

    @RequestMapping(value = "/enable-user-account", method = POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity enableUserAccount(@Validated @RequestBody UserEnableAccountDTO enableAccountDTO, Locale locale) throws OperationNotPermitted, UserNotFoundException {
        User user = userService.enableUserAccount(enableAccountDTO);
        MessageDTO messageDTO;

        if (user.isEnabled()) {
            messageDTO = messageSource.getMessageDTO("user.account.enabled", locale);
        } else {
            messageDTO = messageSource.getMessageDTO("user.account.disabled", locale);
        }

        return new ResponseEntity<>(messageDTO, OK);
    }
}
