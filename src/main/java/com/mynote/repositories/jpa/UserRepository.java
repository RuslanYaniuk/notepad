package com.mynote.repositories.jpa;

import com.mynote.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findAll(Sort var1);

    User findByEmail(String email);

    User findByLogin(String login);

    User findByLoginOrEmail(String login, String email);
}
