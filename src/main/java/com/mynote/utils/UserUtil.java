package com.mynote.utils;

import com.mynote.dto.UserDeleteDTO;
import com.mynote.dto.UserResetPasswordDTO;
import com.mynote.models.User;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class UserUtil {

    public static String getFormattedUserId(UserResetPasswordDTO user) {
        return "'" + user.getId().toString() + "'";
    }

    public static String getFormattedUserId(User user) {
        return "'" + user.getId().toString() + "'";
    }

    public static String getFormattedUserId(UserDeleteDTO user) {
        return "'" + user.getId().toString() + "'";
    }
}
