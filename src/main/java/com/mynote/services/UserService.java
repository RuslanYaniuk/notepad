package com.mynote.services;

import com.mynote.config.ApplicationConfig;
import com.mynote.exceptions.*;
import com.mynote.models.User;
import com.mynote.models.UserRole;
import com.mynote.repositories.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Service
public class UserService {

    private static final int BCRYPT_GENSALT_LOG2_ROUNDS = 12;

    public static final String USER_FIRST_NAME_SUFFIX = "_user_first_name";
    public static final String USER_LAST_NAME_SUFFIX = "_user_last_name";
    public static final String USER_LOGIN_SUFFIX = "_user_login";

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    public User addUser(String email, String password) throws LoginAlreadyTakenException, EmailAlreadyTakenException {
        String login = email.substring(0, email.indexOf('@'));
        User user = new User(login + USER_LOGIN_SUFFIX, email);

        user.setFirstName(login + USER_FIRST_NAME_SUFFIX);
        user.setLastName(login + USER_LAST_NAME_SUFFIX);
        user.setPassword(password);
        return addUser(user);
    }

    public User addUser(User user) throws LoginAlreadyTakenException, EmailAlreadyTakenException {
        return userRepository.save(createUser(user));
    }

    public User addAdministrator(User user) throws LoginAlreadyTakenException, EmailAlreadyTakenException {
        user = createUser(user);
        user.addRole(userRoleService.getRoleAdmin());
        return userRepository.save(user);
    }

    public List<User> getAllUsersSortByLastNameDesc() {
        return userRepository.findAll(new Sort(Sort.Direction.ASC, "lastName"));
    }

    public User findUserById(Long id) throws UserNotFoundException {
        User user;

        if ((user = userRepository.findOne(id)) == null) {
            throw new UserNotFoundException("id: " + id.toString());
        }
        return user;
    }

    public User findUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("email: " + email);
        }
        return user;
    }

    public User findUserByLogin(String login) throws UserNotFoundException {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new UserNotFoundException("login: " + login);
        }
        return user;
    }

    public User findByLoginOrEmail(String login, String email) throws UserNotFoundException {
        User user = userRepository.findByLoginOrEmail(login, email);

        if (user == null) {
            throw new UserNotFoundException("email: " + email + ", login: " + login);
        }
        return user;
    }

    public User updateUser(User user) throws UserNotFoundException, UserRoleNotFoundException {
        Long id = user.getId();
        User existentUser = userRepository.findOne(id);

        if (existentUser == null) {
            throw new UserNotFoundException("id: " + id.toString());
        }
        existentUser.setEmail(user.getEmail().toLowerCase().trim());
        existentUser.setFirstName(user.getFirstName().trim());
        existentUser.setLastName(user.getLastName().trim());
        return userRepository.save(existentUser);
    }

    public User updateUserRoles(User user) throws UserRoleNotFoundException, UserNotFoundException {
        Set<UserRole> userRoles = userRoleService.findRoles(user.getRoles());

        user = findUserById(user.getId());
        user.setRoles(userRoles);
        return userRepository.save(user);
    }

    public void deleteUser(User user) throws OperationNotPermitted, UserNotFoundException {
        user = findUserById(user.getId());

        if (user.equals(getSystemAdministrator())) {
            throw new OperationNotPermitted();
        }
        userRepository.delete(user);
    }

    public User resetUserPassword(User user) throws UserNotFoundException {
        String newPassword = user.getPassword();

        user = findUserById(user.getId());
        user.setPassword(getBCryptHash(newPassword));
        return userRepository.save(user);
    }

    public User enableUserAccount(User user) throws UserNotFoundException, OperationNotPermitted {
        Boolean enabled = user.isEnabled();

        user = findUserById(user.getId());
        if (user.equals(getSystemAdministrator())) {
            throw new OperationNotPermitted();
        }
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    public User getSystemAdministrator() {
        return userRepository.findByLogin(applicationConfig.getAdminLogin());
    }

    private User createUser(User user) throws LoginAlreadyTakenException, EmailAlreadyTakenException {
        String login = user.getLogin().toLowerCase().trim();
        String email = user.getEmail().toLowerCase().trim();
        User existentUser;

        if ((existentUser = userRepository.findByLoginOrEmail(login, email)) != null) {
            if (existentUser.getLogin().equals(login)) {
                throw new LoginAlreadyTakenException(login);
            } else {
                throw new EmailAlreadyTakenException(email);
            }
        }
        user.setLogin(login);
        user.setEmail(email);
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setPassword(getBCryptHash(user.getPassword()));
        user.addRole(userRoleService.getRoleUser());
        user.setEnabled(true);
        return user;
    }

    private String getBCryptHash(String password) {
        String salt = BCrypt.gensalt(BCRYPT_GENSALT_LOG2_ROUNDS);

        return BCrypt.hashpw(password, salt);
    }
}
