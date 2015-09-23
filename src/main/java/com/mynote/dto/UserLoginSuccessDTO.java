package com.mynote.dto;

/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
public class UserLoginSuccessDTO extends MessageDTO {

    private UserDTO userDTO;

    public UserLoginSuccessDTO() {
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
