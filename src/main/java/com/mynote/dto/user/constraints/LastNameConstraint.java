package com.mynote.dto.user.constraints;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class LastNameConstraint extends AbstractUserDTO {

    public LastNameConstraint(User user) {
        super(user);
    }

    @NotBlank
    @Length(min = 1, max = 255, groups = ValidationGroupB.class)
    @SafeHtml(groups = ValidationGroupC.class)
    public String getLastName() {
        return user.getLastName();
    }
}
