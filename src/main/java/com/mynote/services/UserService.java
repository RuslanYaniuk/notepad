package com.mynote.services;

import com.mynote.config.ApplicationProperties;
import com.mynote.dto.*;
import com.mynote.exceptions.*;
import com.mynote.models.User;
import com.mynote.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Service
public class UserService {
    private static final int BCRYPT_GENSALT_LOG2_ROUNDS = 12;

    @Autowired
    private ApplicationProperties appProperties;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    public User getSystemAdministrator() {
        return userRepository.findByLoginOrEmail(appProperties.getAdminLogin(), appProperties.getAdminEmail());
    }

    public User addNewUser(UserRegistrationDTO userRegistrationDTO) throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createUser(userRegistrationDTO);

        return saveUser(user);
    }

    public User addAdministrator(UserRegistrationDTO userRegistrationDTO) throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        User user = createUser(userRegistrationDTO);

        user.addRole(userRoleService.getRoleAdmin());
        return saveUser(user);
    }

    private User createUser(UserRegistrationDTO userRegistrationDTO) throws EmailAlreadyTakenException, LoginAlreadyTakenException {
        String login = userRegistrationDTO.getLogin().toLowerCase().trim();
        String email = userRegistrationDTO.getEmail().toLowerCase().trim();
        User user;
        User existentUser;

        if ((existentUser = userRepository.findByLoginOrEmail(login, email)) != null) {
            if (existentUser.getLogin().equals(login)) {
                throw new LoginAlreadyTakenException(login);
            } else {
                throw new EmailAlreadyTakenException(email);
            }
        }

        user = new User(login, email);
        user.setFirstName(userRegistrationDTO.getFirstName().trim());
        user.setLastName(userRegistrationDTO.getLastName().trim());

        user.setPassword(getBCryptHash(userRegistrationDTO.getPassword()));
        user.setEnabled(true);
        user.addRole(userRoleService.getRoleUser());

        return user;
    }

    public List<User> getAllUsersSortByLastNameDesc() {
        return userRepository.findAll(new Sort(Sort.Direction.ASC, "lastName"));
    }

    @Transactional
    public User updateUser(UserUpdateDTO userUpdateDTO) throws UserNotFoundException, UserRoleNotFoundException {
        User user = findUserById(userUpdateDTO.getId());

        userUpdateDTO.setEmail(userUpdateDTO.getEmail().toLowerCase().trim());
        userUpdateDTO.setFirstName(userUpdateDTO.getFirstName().trim());
        userUpdateDTO.setLastName(userUpdateDTO.getLastName().trim());

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setRoles(userRoleService.findRoles(userUpdateDTO.getUserRoleDTOs()));

        return saveUser(user);
    }

    public User resetUserPassword(UserResetPasswordDTO resetPasswordDTO) throws UserNotFoundException {
        User user = findUserById(resetPasswordDTO.getId());

        user.setPassword(getBCryptHash(resetPasswordDTO.getPassword()));

        return saveUser(user);
    }

    public void deleteUser(UserDeleteDTO userDeleteDTO) throws UserNotFoundException, OperationNotPermitted {
        User userToDelete = findUserById(userDeleteDTO.getId());

        if (userToDelete.equals(getSystemAdministrator())) {
            throw new OperationNotPermitted();
        }
        userRepository.delete(userToDelete);
    }

    public User findUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findOne(id);

        if (user == null) {
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

    public User findByLoginOrEmail(String login, String email) throws UserNotFoundException {
        User user = userRepository.findByLoginOrEmail(login, email);

        if (user == null) {
            throw new UserNotFoundException("email: " + email + ", login: " + login);
        }
        return user;
    }

    public User enableUserAccount(UserEnableAccountDTO enableAccountDTO) throws UserNotFoundException, OperationNotPermitted {
        User user = findUserById(enableAccountDTO.getId());

        if (user.equals(getSystemAdministrator())) {
            throw new OperationNotPermitted();
        }
        user.setEnabled(enableAccountDTO.getEnabled());

        return saveUser(user);
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private String getBCryptHash(String password) {
        String salt = BCrypt.gensalt(BCRYPT_GENSALT_LOG2_ROUNDS);

        return BCrypt.hashpw(password, salt);
    }
}
