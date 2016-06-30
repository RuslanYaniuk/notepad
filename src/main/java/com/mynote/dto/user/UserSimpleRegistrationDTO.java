package com.mynote.dto.user;

import com.mynote.utils.validation.annotations.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
public class UserSimpleRegistrationDTO extends AbstractUserDTO {

    private Boolean signIn;

    public Boolean getSignIn() {
        return signIn;
    }

    public void setSignIn(Boolean signIn) {
        this.signIn = signIn;
    }

    @Email
    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    @NotBlank
    @Length(min = 8, max = 100)
    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }
}
