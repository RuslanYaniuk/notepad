package com.mynote.utils;

import com.mynote.dto.user.UserInfoDTO;
import com.mynote.models.User;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDtoUtil {

    public static UserInfoDTO[] convert(List<User> userList) {
        UserInfoDTO[] userDTOArray = new UserInfoDTO[userList.size()];

        for (int i = 0; i < userList.size(); i++) {
            userDTOArray[i] = new UserInfoDTO(userList.get(i));
        }
        return userDTOArray;
    }
}
