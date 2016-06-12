package com.mynote.services;

import com.mynote.exceptions.UserRoleAlreadyExists;
import com.mynote.exceptions.UserRoleNotFoundException;
import com.mynote.models.UserRole;
import com.mynote.repositories.jpa.UserRoleRepository;
import com.mynote.utils.RoleComparator;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Service
public class UserRoleService {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole getRoleAdmin() {
        return userRoleRepository.findByRole(ROLE_ADMIN);
    }

    public UserRole getRoleUser() {
        return userRoleRepository.findByRole(ROLE_USER);
    }

    @Transactional
    public UserRole initializeUsersSet(UserRole userRole) {
        userRole = userRoleRepository.findOne(userRole.getId());
        Hibernate.initialize(userRole.getUsers());

        return userRole;
    }

    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }

    public Set<UserRole> findRoles(Iterable<UserRole> userRoles) throws UserRoleNotFoundException {
        Set<UserRole> userRoleList = new TreeSet<>(new RoleComparator());

        for (UserRole userRole : userRoles) {
            if ((userRole = userRoleRepository.findByRole(userRole.getRole())) == null) {
                throw new UserRoleNotFoundException();
            }
            userRoleList.add(userRole);
        }
        return userRoleList;
    }

    public UserRole addRole(UserRole userRole) throws UserRoleAlreadyExists {
        if (userRoleRepository.findByRole(userRole.getRole()) != null) {
            throw new UserRoleAlreadyExists();
        }
        return userRoleRepository.save(userRole);
    }
}
