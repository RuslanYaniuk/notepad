package com.mynote.test.utils;

import com.mynote.dto.user.UserDTO;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.dto.user.UserUpdateDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserDtoUtil {

    public static List<UserUpdateDTO> convertToUpdateDTO(UserDTO[] userDTOArray) {
        List<UserUpdateDTO> userUpdateDTOList = new ArrayList<>(userDTOArray.length);

        for (UserDTO userDTO : userDTOArray) {
            UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

            userUpdateDTO.setId(userDTO.getId());
            userUpdateDTO.setEmail(userDTO.getEmail());
            userUpdateDTO.setFirstName(userDTO.getFirstName());
            userUpdateDTO.setLastName(userDTO.getLastName());
            userUpdateDTO.setUserRoleDTOs(userDTO.getUserRoleDTOs());

            userUpdateDTOList.add(userUpdateDTO);
        }

        return userUpdateDTOList;
    }

    public static UserLoginDTO convertToUserLoginDTO(UserRegistrationDTO userRegistrationDTO) {
        return new UserLoginDTO(userRegistrationDTO.getLogin(), userRegistrationDTO.getPassword());
    }

    public static UserRegistrationDTO createSimpleUserRegistrationDTO() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

        userRegistrationDTO.setEmail("valid@email.com");
        userRegistrationDTO.setFirstName("Jim");
        userRegistrationDTO.setLastName("Morrison");
        userRegistrationDTO.setPassword("the789Doors$");
        userRegistrationDTO.setLogin("doorsAreInsane1967");

        return userRegistrationDTO;
    }
}
