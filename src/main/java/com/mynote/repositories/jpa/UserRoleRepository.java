package com.mynote.repositories.jpa;

import com.mynote.models.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

    List<UserRole> findAll();

    UserRole findByRole(String role);
}
