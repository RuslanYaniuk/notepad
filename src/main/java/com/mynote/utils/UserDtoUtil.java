package com.mynote.utils;

import com.mynote.dto.user.UserDTO;
import com.mynote.models.User;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDtoUtil {

    public static UserDTO[] convert(List<User> userList) {
        UserDTO[] userDTOArray = new UserDTO[userList.size()];

        for (int i = 0; i < userList.size(); i++) {
            userDTOArray[i] = convert(userList.get(i));
        }
        return userDTOArray;
    }

    public static UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO(user);

        userDTO.setUserRoleDTOs(UserRoleDtoUtil.convert(user.getRoles()));

        return userDTO;
    }
}
