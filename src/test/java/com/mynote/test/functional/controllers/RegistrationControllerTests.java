package com.mynote.test.functional.controllers;

import com.mynote.dto.user.UserFindDTO;
import com.mynote.dto.user.UserLoginDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import com.mynote.dto.user.UserSimpleRegistrationDTO;
import com.mynote.exceptions.EmailAlreadyTakenException;
import com.mynote.exceptions.LoginAlreadyTakenException;
import com.mynote.exceptions.ValidationException;
import com.mynote.models.User;
import com.mynote.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpSession;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserTestUtils.createNonExistentUser;
import static com.mynote.test.utils.UserTestUtils.getUser2;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@RunWith(Enclosed.class)
public class RegistrationControllerTests {

    public static final String CHARACTERS_300 = StringUtils.leftPad("a", 300, "a");
    public static final String HTML = "<script>alert('I can steal cookies')</script>";
    public static final String[] NON_VALID_LENGTH = new String[]{
            "1234567",
            CHARACTERS_300
    };
    public static final String[] NULL_EMPTY_STRINGS = new String[]{
            "",
            "           ",
            null
    };
    public static final String PASSWORD_CODE = "Password";
    public static final String[] NON_VALID_EMAILS = new String[]{
            "email98WithoutAtSymbol",
            "email78WithoutDNSName@",
            "@NoAddress8",
            "emailWithoutDomain@somewhere.",
            "emai98lDomainLessThan2Characters@somewhere.a",
            "@NoAddress.com",
            "@NoAddress.c",
            "email33@NoAddress.789",
            "адресс@пошта.ком",
            "<script>alert('I can steal cookies')</script>@mail.com"
    };
    public static final String[] NON_VALID_PASSWORDS = new String[]{
            "alllowercas4e#",
            "!!ALLUPPERCASE88",
            "nospecialSymboLs123",
            "!#@#nodigitS",
            "noDigitS",
            "!#@#456456",
            "юнікод@#$234"
    };
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String LOGIN_FIELD_NAME = "login";
    public static final String PASSWORD_FIELD_NAME = "password";
    public static final String FIRST_NAME_FIELD_NAME = "firstName";
    public static final String LAST_NAME_FIELD_NAME = "lastName";

    public static class RegistrationTests extends AbstractControllerTest {

        private UserRegistrationDTO userRegistrationDTO;
        private UserFindDTO userFindDTO;

        @Before
        public void setup() throws Exception {
            dbUnit.deleteAllFixtures();

            dbUnit.insertUsers();
            dbUnit.insertRoles();
            dbUnit.insertUsersToRoles();

            userRegistrationDTO = createRegistrationDTO();
            userFindDTO = new UserFindDTO();
        }

        @Test
        public void registerNewUser_ValidUserDetails_RegistrationSuccess() throws Exception {
            registerNewUser(userRegistrationDTO)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is(userRegistrationDTO.getEmail())))
                    .andExpect(jsonPath("$.login", is(userRegistrationDTO.getLogin().toLowerCase())))
                    .andExpect(jsonPath("$.registrationDateUTC", not(emptyString())));
        }

        @Test
        public void registerNewUser_NonValidEmail_BadRequestReturned() throws Exception {
            String[] nonValidLengthEmails = new String[]{
                    "a@b",
                    CHARACTERS_300 + "@email.com"
            };

            for (String email : NON_VALID_EMAILS) {
                userRegistrationDTO.setEmail(email);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(EMAIL_FIELD_NAME), hasItems(EMAIL_CODE)));
            }
            for (String email : nonValidLengthEmails) {
                userRegistrationDTO.setEmail(email);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(EMAIL_FIELD_NAME), hasItems(LENGTH_CODE)));
            }
            for (String email : NULL_EMPTY_STRINGS) {
                userRegistrationDTO.setEmail(email);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(EMAIL_FIELD_NAME), hasItems(NOT_BLANK_CODE)));
            }
        }

        @Test
        public void registerNewUser_AlreadyTakenEmail_BadRequestReturned() throws Exception {
            userRegistrationDTO.setEmail(getUser2().getEmail());
            registerNewUser(userRegistrationDTO)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath($_TYPE, is(EmailAlreadyTakenException.class.getSimpleName())))
                    .andExpect(jsonPath(getFieldErrorCodes(EMAIL_FIELD_NAME), hasItem(TAKEN_EMAIL_CODE)));
        }

        @Test
        public void registerNewUser_NonValidLogin_BadRequestReturned() throws Exception {
            String[] invalidLogins = new String[]{
                    "@Login!!IsNotValid132",
                    "LoginSeems_valid123\n" + "newline",
                    "LoginSeems valid123",
                    HTML
            };

            for (String login : invalidLogins) {
                userRegistrationDTO.setLogin(login);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(LOGIN_FIELD_NAME), hasItems(PATTERN_CODE)));
            }
            for (String login : NON_VALID_LENGTH) {
                userRegistrationDTO.setLogin(login);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(LOGIN_FIELD_NAME), hasItems(LENGTH_CODE)));
            }
            for (String login : NULL_EMPTY_STRINGS) {
                userRegistrationDTO.setLogin(login);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(LOGIN_FIELD_NAME), hasItems(NOT_BLANK_CODE)));
            }
        }

        @Test
        public void registerNewUser_AlreadyTakenLogin_BadRequestReturned() throws Exception {
            userRegistrationDTO.setLogin(getUser2().getLogin());
            registerNewUser(userRegistrationDTO)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath($_TYPE, is(LoginAlreadyTakenException.class.getSimpleName())))
                    .andExpect(jsonPath(getFieldErrorCodes(LOGIN_FIELD_NAME), hasItems(TAKEN_LOGIN_CODE)));
        }

        @Test
        public void registerNewUser_NonValidPassword_BadRequestReturned() throws Exception {
            for (String password : NON_VALID_PASSWORDS) {
                userRegistrationDTO.setPassword(password);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(PASSWORD_FIELD_NAME), hasItems(PASSWORD_CODE)));
            }
            for (String password : NON_VALID_LENGTH) {
                userRegistrationDTO.setPassword(password);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(PASSWORD_FIELD_NAME), hasItems(LENGTH_CODE)));
            }
            for (String password : NULL_EMPTY_STRINGS) {
                userRegistrationDTO.setPassword(password);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(PASSWORD_FIELD_NAME), hasItems(NOT_BLANK_CODE)));
            }
        }

        @Test
        public void registerNewUser_NonValidFirstName_BadRequestReturned() throws Exception {
            for (String firstName : NULL_EMPTY_STRINGS) {
                userRegistrationDTO.setFirstName(firstName);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(FIRST_NAME_FIELD_NAME), hasItem(NOT_BLANK_CODE)));
            }
            userRegistrationDTO.setFirstName(CHARACTERS_300);
            registerUserWithValidationViolation(userRegistrationDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(FIRST_NAME_FIELD_NAME), hasItem(LENGTH_CODE)));

            userRegistrationDTO.setFirstName(HTML);
            registerUserWithValidationViolation(userRegistrationDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(FIRST_NAME_FIELD_NAME), hasItem(NON_SAFE_HTML_CODE)));
        }

        @Test
        public void registerNewUser_NonValidLastName_BadRequestReturned() throws Exception {
            for (String lastName : NULL_EMPTY_STRINGS) {
                userRegistrationDTO.setLastName(lastName);
                registerUserWithValidationViolation(userRegistrationDTO)
                        .andExpect(jsonPath(getFieldErrorCodes(LAST_NAME_FIELD_NAME), hasItems(NOT_BLANK_CODE)));
            }
            userRegistrationDTO.setLastName(CHARACTERS_300);
            registerUserWithValidationViolation(userRegistrationDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(LAST_NAME_FIELD_NAME), hasItems(LENGTH_CODE)));

            userRegistrationDTO.setLastName(HTML);
            registerUserWithValidationViolation(userRegistrationDTO)
                    .andExpect(jsonPath(getFieldErrorCodes(LAST_NAME_FIELD_NAME), hasItems(NON_SAFE_HTML_CODE)));
        }

        @Test
        public void checkAvailableEmail_NewEmail_OkReturnedWithAvailableMessage() throws Exception {
            userFindDTO.setEmail(createNonExistentUser().getEmail());
            checkAvailable(userFindDTO)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath($_MESSAGE_CODE, is("user.email.isAvailable")));
        }

        @Test
        public void checkAvailableEmail_RegisteredEmail_OkReturnedWithNotAvailableMessage() throws Exception {
            userFindDTO.setEmail(getUser2().getEmail());
            checkAvailable(userFindDTO)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath($_MESSAGE_CODE, is("user.email.isNotAvailable")));
        }

        @Test
        public void checkAvailableLogin_NewLogin_OkReturnedWithAvailableMessage() throws Exception {
            userFindDTO.setLogin(createNonExistentUser().getLogin());
            checkAvailable(userFindDTO)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath($_MESSAGE_CODE, is("user.login.isAvailable")));
        }

        @Test
        public void checkAvailableLogin_RegisteredLogin_OkReturnedWithNotAvailableMessage() throws Exception {
            userFindDTO.setLogin(getUser2().getLogin());
            checkAvailable(userFindDTO)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath($_MESSAGE_CODE, is("user.login.isNotAvailable")));
        }

        @Test
        public void simpleRegistration_EmailAndPassword_OkUserRegistered() throws Exception {
            UserSimpleRegistrationDTO userSimpleRegistrationDTO = new UserSimpleRegistrationDTO();
            User user = createNonExistentUser();
            String email = user.getEmail();

            user.setPassword("12345678");
            userSimpleRegistrationDTO.setUser(user);
            mockMvc.perform(put("/api/registration/simple")
                    .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                    .content(jacksonObjectMapper.writeValueAsString(userSimpleRegistrationDTO)))
                    .andExpect(jsonPath("$.email", is(email)))
                    .andExpect(jsonPath("$.login", is("nonexistent" + UserService.USER_LOGIN_SUFFIX)))
                    .andExpect(jsonPath("$.registrationDateUTC", not(emptyString())));
        }

        private ResultActions registerUserWithValidationViolation(UserRegistrationDTO userRegistrationDTO) throws Exception {
            return registerNewUser(userRegistrationDTO)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath($_TYPE, is(ValidationException.class.getSimpleName())));
        }

        private ResultActions registerNewUser(UserRegistrationDTO userRegistrationDTO) throws Exception {
            return mockMvc.perform(put("/api/registration/full")
                            .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                            .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
            );
        }

        private ResultActions checkAvailable(UserFindDTO userFindDTO) throws Exception {
            return mockMvc.perform(post("/api/registration/check-available")
                            .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                            .content(jacksonObjectMapper.writeValueAsString(userFindDTO))
            );
        }

        private UserRegistrationDTO createRegistrationDTO() {
            UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

            userRegistrationDTO.setUser(createNonExistentUser());
            return userRegistrationDTO;
        }
    }

    public static class SecuredRegistrationTests extends AbstractSecuredControllerTest {

        @Before
        public void setup() throws Exception {
            dbUnit.deleteAllFixtures();

            dbUnit.insertUsers();
            dbUnit.insertRoles();
            dbUnit.insertUsersToRoles();
        }

        @Test
        public void afterSuccessRegistrationUserCanLogin() throws Exception {
            User user = createNonExistentUser();
            UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
            UserLoginDTO loginDTO;

            registrationDTO.setUser(user);
            registerNewUser(registrationDTO).andExpect(status().isOk());

            loginDTO = new UserLoginDTO();
            loginDTO.setUser(user);
            loginUser(loginDTO).andExpect(status().isOk());
        }

        @Test
        public void simpleRegistration_SignInFlagSet_OkUserRegisteredAndSignedIn() throws Exception {
            UserSimpleRegistrationDTO userSimpleRegistrationDTO = new UserSimpleRegistrationDTO();
            User user = createNonExistentUser();
            HttpSession session;

            user.setPassword("12345678");
            userSimpleRegistrationDTO.setUser(user);
            userSimpleRegistrationDTO.setSignIn(true);
            session = mockMvc.perform(put("/api/registration/simple").with(csrf())
                    .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                    .content(jacksonObjectMapper.writeValueAsString(userSimpleRegistrationDTO)))
                    .andExpect(jsonPath("$.email", is(user.getEmail())))
                    .andReturn().getRequest().getSession();

            mockMvc.perform(get("/api/user/get-user-info").session((MockHttpSession) session))
                    .andExpect(status().isOk());
        }

        private ResultActions registerNewUser(UserRegistrationDTO userRegistrationDTO) throws Exception {
            return mockMvc.perform(put("/api/registration/full").with(csrf())
                            .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                            .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
            );
        }
    }
}
