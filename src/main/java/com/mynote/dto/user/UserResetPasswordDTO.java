package com.mynote.dto.user;

import com.mynote.config.validation.ValidationGroupB;
import com.mynote.config.validation.ValidationGroupC;
import com.mynote.config.validation.annotations.Password;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserResetPasswordDTO {

    @NotNull
    private Long id;

    @NotBlank
    @Length(min = 8, max = 100, groups = ValidationGroupB.class)
    @Password(groups = ValidationGroupC.class)
    private String password;

    public UserResetPasswordDTO() {
    }

    public UserResetPasswordDTO(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserResetPasswordDTO that = (UserResetPasswordDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(password != null ? !password.equals(that.password) : that.password != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserResetPasswordDTO{" +
                "id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}
