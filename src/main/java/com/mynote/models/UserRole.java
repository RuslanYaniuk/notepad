package com.mynote.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Entity
@Table(name = "user_roles")
public class UserRole implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = 5277205406734364062L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 191)
    private String role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public UserRole() {
    }

    public UserRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() == o.getClass()) {
            return role.equals(((UserRole) o).role);
        }
        if (o.getClass() == GrantedAuthority.class || o.getClass() == SimpleGrantedAuthority.class) {
            return role.equals(((GrantedAuthority) o).getAuthority());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "role='" + role + '\'' +
                ", id=" + id +
                '}';
    }
}
