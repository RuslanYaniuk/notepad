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
import com.mynote.utils.UserRoleDtoUtil;
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
        return ok(UserRoleDtoUtil.convert(userRoleService.getAllUserRoles()));
    }

    @RequestMapping(value = "/update-user", method = POST)
    public ResponseEntity updateUser(@Validated @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException, UserRoleNotFoundException {
        UserUpdateDTO userUpdateDTOResponse = new UserUpdateDTO(userService.updateUser(userUpdateDTO));

        return ok(userUpdateDTOResponse);
    }

    @RequestMapping(value = "/delete-user", method = DELETE)
    public ResponseEntity deleteUser(@Validated @RequestBody UserDeleteDTO userDeleteDTO) throws UserNotFoundException, OperationNotPermitted {
        userService.deleteUser(userDeleteDTO);

        return messageOK("user.account.deleted");
    }

    @RequestMapping(value = "/find-user", method = POST)
    public ResponseEntity findUser(@RequestBody UserFindDTO userFindDTO) throws UserNotFoundException, SearchFieldsAreEmpty {
        Long userId = userFindDTO.getId();

        if (userFindDTO.getId() != null) {
            UserDTO userDTO = UserDtoUtil.convert(userService.findUserById(userId));

            return ok(userDTO);
        }

        throw new SearchFieldsAreEmpty();
    }

    @RequestMapping(value = "/reset-user-password", method = POST)
    public ResponseEntity resetUserPassword(@Validated @RequestBody UserResetPasswordDTO resetPasswordDTO) throws UserNotFoundException {
        User user = userService.resetUserPassword(resetPasswordDTO);

        return messageOK("user.reset.password.success", user.getId().toString());
    }

    @RequestMapping(value = "/enable-user-account", method = POST)
    public ResponseEntity enableUserAccount(@Validated @RequestBody UserEnableAccountDTO enableAccountDTO) throws OperationNotPermitted, UserNotFoundException {
        User user = userService.enableUserAccount(enableAccountDTO);

        if (user.isEnabled()) {
            return messageOK("user.account.enabled");
        } else {
            return messageOK("user.account.disabled");
        }
    }
}
