package com.mynote.controllers;

import com.mynote.dto.user.*;
import com.mynote.exceptions.OperationNotPermitted;
import com.mynote.exceptions.SearchFieldsAreEmpty;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.exceptions.UserRoleNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserRoleService;
import com.mynote.services.UserService;
import com.mynote.utils.UserDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RestController
@RequestMapping("/api/administration")
public class AdministrationController extends AbstractController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @RequestMapping(value = "/list-all-users", method = GET)
    public ResponseEntity listAllUsers() throws IOException {
        List<User> userList = userService.getAllUsersSortByLastNameDesc();

        return ok(UserDtoUtil.convert(userList));
    }

    @RequestMapping(value = "/get-all-user-roles", method = GET)
    public ResponseEntity getAllUserRoles() throws IOException {
        return ok(userRoleService.getAllUserRoles());
    }

    @RequestMapping(value = "/update-user", method = POST)
    public ResponseEntity updateUser(@Validated @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException, UserRoleNotFoundException {
        userService.updateUser(userUpdateDTO.getUser());
        return messageOK("user.update.success");
    }

    @RequestMapping(value = "/delete-user", method = DELETE)
    public ResponseEntity deleteUser(@Validated @RequestBody UserDeleteDTO userDeleteDTO) throws UserNotFoundException, OperationNotPermitted {
        userService.deleteUser(userDeleteDTO.getUser());
        return messageOK("user.account.deleted");
    }

    @RequestMapping(value = "/find-user", method = POST)
    public ResponseEntity findUser(@Validated @RequestBody UserFindDTO userFindDTO) throws UserNotFoundException, SearchFieldsAreEmpty {
        Long userId = userFindDTO.getId();

        if (userFindDTO.getId() != null) {
            UserInfoDTO userDTO = new UserInfoDTO(userService.findUserById(userId));
            return ok(userDTO);
        }
        throw new SearchFieldsAreEmpty();
    }

    @RequestMapping(value = "/reset-user-password", method = POST)
    public ResponseEntity resetUserPassword(@Validated @RequestBody UserResetPasswordDTO resetPasswordDTO) throws UserNotFoundException {
        User user = userService.resetUserPassword(resetPasswordDTO.getUser());

        return messageOK("user.reset.password.success", user.getId().toString());
    }

    @RequestMapping(value = "/enable-user-account", method = POST)
    public ResponseEntity enableUserAccount(@Validated @RequestBody UserEnableDTO enableAccountDTO) throws OperationNotPermitted, UserNotFoundException {
        User user = userService.enableUserAccount(enableAccountDTO.getUser());

        if (user.isEnabled()) {
            return messageOK("user.account.enabled");
        } else {
            return messageOK("user.account.disabled");
        }
    }

    @RequestMapping(value = "/update-user-roles", method = POST)
    public ResponseEntity updateUserRoles(@Validated @RequestBody UserUpdateRolesDTO userUpdateRolesDTO) throws UserRoleNotFoundException, UserNotFoundException {
        userService.updateUserRoles(userUpdateRolesDTO.getUser());
        return messageOK("user.roles.updated");
    }
}
