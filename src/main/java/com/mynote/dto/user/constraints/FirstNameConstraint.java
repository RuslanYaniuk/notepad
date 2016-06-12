package com.mynote.dto.user.constraints;

import com.mynote.dto.user.AbstractUserDTO;
import com.mynote.models.User;
import com.mynote.utils.validation.ValidationGroupB;
import com.mynote.utils.validation.ValidationGroupC;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
public class FirstNameConstraint extends AbstractUserDTO {

    public FirstNameConstraint(User user) {
        super(user);
    }

    @NotBlank
    @Length(min = 1, max = 255, groups = ValidationGroupB.class)
    @SafeHtml(groups = ValidationGroupC.class)
    public String getFirstName() {
        return user.getFirstName();
    }
}
