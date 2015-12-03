package com.mynote.test.unit.controllers;

import com.mynote.dto.user.UserFindDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserTestUtils.createNonExistentUser;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class RegistrationControllerTests extends AbstractControllerTest {

    // Error codes
    public static final String EMAIL_FORMAT_CODE = "Email.userRegistrationDTO.emailConstraint.email";
    public static final String EMAIL_LENGTH_CODE = "Length.userRegistrationDTO.emailConstraint.email";
    public static final String EMAIL_BLANK_CODE = "NotBlank.userRegistrationDTO.emailConstraint.email";

    public static final String PASSWORD_NOT_BLANK_CODE = "NotBlank.userRegistrationDTO.passwordConstraint.password";
    public static final String PASSWORD_PATTERN_CODE = "Password.userRegistrationDTO.passwordConstraint.password";
    public static final String PASSWORD_LENGTH_CODE = "Length.userRegistrationDTO.passwordConstraint.password";

    public static final String FIRST_NAME_NOT_BLANK_CODE = "NotBlank.userRegistrationDTO.firstNameConstraint.firstName";
    public static final String FIRST_NAME_SAFE_HTML_CODE = "SafeHtml.userRegistrationDTO.firstNameConstraint.firstName";
    public static final String FIRST_NAME_LENGTH_CODE = "Length.userRegistrationDTO.firstNameConstraint.firstName";

    public static final String LAST_NAME_NOT_BLANK_CODE = "NotBlank.userRegistrationDTO.lastNameConstraint.lastName";
    public static final String LAST_NAME_SAFE_HTML_CODE = "SafeHtml.userRegistrationDTO.lastNameConstraint.lastName";
    public static final String LAST_NAME_LENGTH_CODE = "Length.userRegistrationDTO.lastNameConstraint.lastName";

    public static final String LOGIN_NOT_BLANK_CODE = "NotBlank.userRegistrationDTO.loginConstraint.login";
    public static final String LOGIN_PATTERN_CODE = "Pattern.userRegistrationDTO.loginConstraint.login";
    public static final String LOGIN_LENGTH_CODE = "Length.userRegistrationDTO.loginConstraint.login";

    @Before
    public void setup() throws Exception {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void registerNewUser_ValidUserDetails_RegistrationSuccess() throws Exception {
        registerNewUser(createRegistrationDTO())
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.registration.success")));
    }

    @Test
    public void registerNewUser_BlankOrEmptyEmail_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setEmail("");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_BLANK_CODE)));

        userRegistrationDTO.setEmail(" ");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_BLANK_CODE)));

        userRegistrationDTO.setEmail(null);
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_BLANK_CODE)));
    }

    @Test
    public void registerNewUser_NotValidEmailFormat_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setEmail("email98WithoutAtSymbol");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("email78WithoutDNSName@");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("@NoAddress8");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("emailWithoutDomain@somewhere.");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("emai98lDomainLessThan2Characters@somewhere.a");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("@NoAddress.com");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("@NoAddress.c");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("email33@NoAddress.789");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO.setEmail("адресс@пошта.ком");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));
    }

    @Test
    public void registerNewUser_BlankOrEmptyPassword_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setPassword("");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_NOT_BLANK_CODE)));

        userRegistrationDTO.setPassword("  ");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_NOT_BLANK_CODE)));

        userRegistrationDTO.setPassword(null);
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_NOT_BLANK_CODE)));
    }

    @Test
    public void registerNewUser_PasswordDoesNotMuchComplexity_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setPassword("alllowercas4e#");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));

        userRegistrationDTO.setPassword("!!ALLUPPERCASE88");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));

        userRegistrationDTO.setPassword("nospecialSymboLs123");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));

        userRegistrationDTO.setPassword("!#@#nodigitS");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));

        userRegistrationDTO.setPassword("noDigitS");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));

        userRegistrationDTO.setPassword("!#@#456456");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));

        userRegistrationDTO.setPassword("юнікод@#$234");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_PATTERN_CODE)));
    }

    @Test
    public void registerNewUser_BlankOrEmptyFirstName_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setFirstName("");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(FIRST_NAME_NOT_BLANK_CODE)));

        userRegistrationDTO.setFirstName(" ");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(FIRST_NAME_NOT_BLANK_CODE)));

        userRegistrationDTO.setFirstName(null);
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(FIRST_NAME_NOT_BLANK_CODE)));
    }

    @Test
    public void registerNewUser_BlankOrEmptyLastName_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setLastName("");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LAST_NAME_NOT_BLANK_CODE)));

        userRegistrationDTO.setLastName("  ");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LAST_NAME_NOT_BLANK_CODE)));

        userRegistrationDTO.setLastName(null);
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LAST_NAME_NOT_BLANK_CODE)));
    }

    @Test
    public void registerNewUser_BlankOrEmptyLogin_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setLogin("");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_NOT_BLANK_CODE)));

        userRegistrationDTO.setLogin("  ");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_NOT_BLANK_CODE)));

        userRegistrationDTO.setLogin(null);
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_NOT_BLANK_CODE)));
    }

    @Test
    public void registerNewUser_LoginWithSpecialCharacters_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setLogin("@Login!!IsNotValid132");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_PATTERN_CODE)));
    }

    @Test
    public void registerNewUser_LoginWithNewLineCharacter_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setLogin("LoginSeems_valid123\n" +
                "newline");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_PATTERN_CODE)));
    }

    @Test
    public void registerNewUser_LoginSpaceInside_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setLogin("LoginSeems valid123");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_PATTERN_CODE)));
    }

    @Test
    public void registerNewUser_AlreadyTakenEmail_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();

        registerNewUser(registrationDTO).andExpect(status().isOk());

        registrationDTO.setLogin("differentLogin");

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.registration.error.alreadyTakenEmail")));
    }

    @Test
    public void registerNewUser_AlreadyTakenLogin_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();

        registerNewUser(registrationDTO).andExpect(status().isOk());

        registrationDTO.setEmail("different@email.com");

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.registration.error.alreadyTakenLogin")));
    }

    @Test
    public void registerNewUser_LoginLongerThan191Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 192; i++) {
            sb.append("a");
        }
        registrationDTO.setLogin(sb.toString());

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_EmailLongerThan191Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 64; i++) {
            sb.append("a");
        }
        sb.append("@");
        for (int i = 0; i < 123; i++) {
            sb.append("b");
        }
        sb.append(".com");

        registrationDTO.setEmail(sb.toString());

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_EmailLessThan5Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();

        registrationDTO.setEmail("a@b");

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_LoginLessThan8Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();

        registrationDTO.setLogin("1234567");

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_PasswordLessThan8Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();

        registrationDTO.setPassword("1234567");

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(PASSWORD_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_FirstNameMoreThan255Character_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 256; i++) {
            sb.append("a");
        }

        registrationDTO.setFirstName(sb.toString());

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(FIRST_NAME_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_LastNameMoreThan255Character_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createRegistrationDTO();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 256; i++) {
            sb.append("a");
        }

        registrationDTO.setLastName(sb.toString());

        registerNewUser(registrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LAST_NAME_LENGTH_CODE)));
    }

    @Test
    public void registerNewUser_NotHtmlSafeInput_BadRequest() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createRegistrationDTO();

        userRegistrationDTO.setEmail("<script>alert('I can steal cookies')</script>@mail.com");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(EMAIL_FORMAT_CODE)));

        userRegistrationDTO = createRegistrationDTO();
        userRegistrationDTO.setLogin("<script>alert('I can steal cookies')</script>");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LOGIN_PATTERN_CODE)));

        userRegistrationDTO = createRegistrationDTO();
        userRegistrationDTO.setFirstName("<script>alert('I can steal cookies')</script>");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(FIRST_NAME_SAFE_HTML_CODE)));

        userRegistrationDTO = createRegistrationDTO();
        userRegistrationDTO.setLastName("<script>alert('I can steal cookies')</script>");
        registerNewUser(userRegistrationDTO)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(MESSAGE_CODE, is(LAST_NAME_SAFE_HTML_CODE)));
    }

    @Test
    public void checkAvailableEmail_NotExistentEmail_OkReturnedWithAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setEmail("not@exist.com");

        checkAvailable(userFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.email.isAvailable")));
    }

    @Test
    public void checkAvailableEmail_ExistentEmail_OkReturnedWithNotAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setEmail("user2@email.com");

        checkAvailable(userFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.email.isNotAvailable")));
    }

    @Test
    public void checkAvailableEmail_NotExistentLogin_OkReturnedWithAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setLogin("notExist");

        checkAvailable(userFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.login.isAvailable")));
    }

    @Test
    public void checkAvailableEmail_ExistentLogin_OkReturnedWithNotAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setLogin("user2");

        checkAvailable(userFindDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath(MESSAGE_CODE, is("user.login.isNotAvailable")));
    }

    private ResultActions registerNewUser(UserRegistrationDTO userRegistrationDTO) throws Exception {
        return mockMvc.perform(put("/api/registration/register-new-user")
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
