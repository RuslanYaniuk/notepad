package com.mynote.config.security;

import com.mynote.exceptions.AccountIsDisabledException;
import com.mynote.exceptions.UserNotFoundException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String incomingPassword = authentication.getCredentials().toString();
        String incomingLogin = authentication.getName().toLowerCase();
        UsernamePasswordAuthenticationToken authenticationToken;
        User user;

        try {
            user = userService.findByLoginOrEmail(incomingLogin, incomingLogin);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("user.login.error.incorrectLoginPassword");
        }

        if (!user.isEnabled()) {
            throw new AccountIsDisabledException();
        }

        if (!bCryptPasswordEncoder.matches(incomingPassword, user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("user.login.error.incorrectLoginPassword");
        }

        authenticationToken = new UsernamePasswordAuthenticationToken(incomingLogin, incomingPassword, user.getRoles());
        authenticationToken.setDetails(user.getId());

        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
