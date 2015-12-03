package com.mynote.dto.user.constraints;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import com.mynote.config.validation.annotations.Password;
import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class PasswordConstraint extends AbstractUserDTO {

    public PasswordConstraint(User user) {
        super(user);
    }

    @NotBlank
    @Length(min = 8, max = 100, groups = ValidationGroupB.class)
    @Password(groups = ValidationGroupC.class)
    public String getPassword() {
        return user.getPassword();
    }
}
