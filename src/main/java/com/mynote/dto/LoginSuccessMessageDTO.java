package com.mynote.dto;

import com.mynote.dto.user.UserInfoDTO;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class LoginSuccessMessageDTO extends MessageDTO {

    private UserInfoDTO userDTO;

    public LoginSuccessMessageDTO() {
    }

    public UserInfoDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserInfoDTO userDTO) {
        this.userDTO = userDTO;
    }
}
