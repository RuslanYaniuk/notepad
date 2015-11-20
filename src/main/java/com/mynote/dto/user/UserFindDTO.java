package com.mynote.dto.user;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserFindDTO {

    private Long id;
    private String email;
    private String login;

    public UserFindDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFindDTO that = (UserFindDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return !(login != null ? !login.equals(that.login) : that.login != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserFindDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
