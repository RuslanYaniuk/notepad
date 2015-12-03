package com.mynote.dto.user.constraints;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class EmailConstraint extends AbstractUserDTO {

    public EmailConstraint(User user) {
        super(user);
    }

    @NotBlank
    @Length(min = 5, max = 191, groups = ValidationGroupB.class)
    @Email(
            regexp = "(?i)^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$",
            groups = ValidationGroupC.class
    )
    public String getEmail() {
        return user.getEmail();
    }
}
