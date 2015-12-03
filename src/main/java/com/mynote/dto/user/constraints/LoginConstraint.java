package com.mynote.dto.user.constraints;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class LoginConstraint extends AbstractUserDTO {

    public LoginConstraint(User user) {
        super(user);
    }

    @NotBlank
    @Length(min = 8, max = 191, groups = ValidationGroupB.class)
    @Pattern(regexp = "(?i)^[a-z0-9_-]{8,}$", groups = ValidationGroupC.class)
    public String getLogin() {
        return user.getLogin();
    }
}
