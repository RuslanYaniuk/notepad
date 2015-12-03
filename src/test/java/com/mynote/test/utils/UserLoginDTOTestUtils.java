package com.mynote.test.utils;

import com.mynote.dto.user.UserLoginDTO;

import static com.mynote.test.utils.UserTestUtils.getUser2;
import static com.mynote.test.utils.UserTestUtils.getUserAdmin;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class UserLoginDTOTestUtils {

    public static UserLoginDTO createUserLoginDTO() {
        UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setUser(getUser2());
        loginDTO.setPassword("Passw0rd");

        return loginDTO;
    }

    public static UserLoginDTO createAdminLoginDTO() {
        UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setUser(getUserAdmin());
        loginDTO.setPassword("Passw0rd");

        return loginDTO;
    }

    public static UserLoginDTO createDisabledUserLoginDTO() {
        UserLoginDTO loginDTO = new UserLoginDTO();

        loginDTO.setLogin("user6@email.com");
        loginDTO.setPassword("Passw0rd");

        return loginDTO;
    }
}
