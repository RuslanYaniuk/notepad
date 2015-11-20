package com.mynote.test.unit.controllers;

import com.mynote.dto.user.UserFindDTO;
import com.mynote.dto.user.UserRegistrationDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static com.mynote.test.utils.UserDtoUtil.createSimpleUserRegistrationDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class RegistrationControllerTests extends AbstractControllerTest {

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();

        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void registerNewUser_ValidUserDetails_RegistrationSuccess() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        MvcResult result = registerNewUser(userRegistrationDTO).andExpect(status().isOk()).andReturn();
        checkReturnedMessageCode(result, "user.registration.success");
    }

    @Test
    public void registerNewUser_EmailInUpperCase_RegistrationSuccess() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        userRegistrationDTO.setEmail("UPPERCASE@EMAIL.COM");

        MvcResult result = registerNewUser(userRegistrationDTO).andExpect(status().isOk()).andReturn();
        checkReturnedMessageCode(result, "user.registration.success");
    }

    @Test
    public void registerNewUser_LoginInUpperCase_RegistrationSuccess() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();

        userRegistrationDTO.setLogin("DoorsAREInsAne_1967");

        MvcResult result = registerNewUser(userRegistrationDTO).andExpect(status().isOk()).andReturn();
        checkReturnedMessageCode(result, "user.registration.success");
    }

    @Test
    public void registerNewUser_BlankOrEmptyEmail_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setEmail("");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.email");

        userRegistrationDTO.setEmail(" ");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.email");

        userRegistrationDTO.setEmail(null);
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.email");
    }

    @Test
    public void registerNewUser_NotValidEmailFormat_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setEmail("email98WithoutAtSymbol");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("email78WithoutDNSName@");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("@NoAddress8");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("emailWithoutDomain@somewhere.");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("emai98lDomainLessThan2Characters@somewhere.a");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("@NoAddress.com");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("@NoAddress.c");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("email33@NoAddress.789");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO.setEmail("адресс@пошта.ком");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");
    }

    @Test
    public void registerNewUser_BlankOrEmptyPassword_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setPassword("");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("  ");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.password");

        userRegistrationDTO.setPassword(null);
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.password");
    }

    @Test
    public void registerNewUser_PasswordDoesNotMuchComplexity_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setPassword("alllowercas4e#");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("!!ALLUPPERCASE88");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("nospecialSymboLs123");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("!#@#nodigitS");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("noDigitS");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("!#@#456456");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");

        userRegistrationDTO.setPassword("юнікод@#$234");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Password.userRegistrationDTO.password");
    }

    @Test
    public void registerNewUser_BlankOrEmptyFirstName_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setFirstName("");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.firstName");

        userRegistrationDTO.setFirstName(" ");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.firstName");

        userRegistrationDTO.setFirstName(null);
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.firstName");
    }

    @Test
    public void registerNewUser_BlankOrEmptyLastName_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setLastName("");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.lastName");

        userRegistrationDTO.setLastName("  ");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.lastName");

        userRegistrationDTO.setLastName(null);
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.lastName");
    }

    @Test
    public void registerNewUser_BlankOrEmptyLogin_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setLogin("");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.login");

        userRegistrationDTO.setLogin("  ");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.login");

        userRegistrationDTO.setLogin(null);
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "NotBlank.userRegistrationDTO.login");
    }

    @Test
    public void registerNewUser_LoginWithSpecialCharacters_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setLogin("@Login!!IsNotValid132");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Pattern.userRegistrationDTO.login");
    }

    @Test
    public void registerNewUser_LoginWithNewLineCharacter_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setLogin("LoginSeems_valid123\n" +
                "newline");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Pattern.userRegistrationDTO.login");
    }

    @Test
    public void registerNewUser_LoginSpaceInside_BadRequestReturned() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setLogin("LoginSeems valid123");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Pattern.userRegistrationDTO.login");
    }

    @Test
    public void registerNewUser_AlreadyTakenEmail_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        registerNewUser(registrationDTO).andExpect(status().isOk());

        registrationDTO.setLogin("differentLogin");

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "user.registration.error.alreadyTakenEmail");
    }

    @Test
    public void registerNewUser_AlreadyTakenLogin_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;
        String login = registrationDTO.getLogin().toLowerCase();

        registerNewUser(registrationDTO).andExpect(status().isOk());

        registrationDTO.setEmail("different@email.com");

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "user.registration.error.alreadyTakenLogin");
    }

    @Test
    public void registerNewUser_LoginLongerThan191Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        StringBuilder sb = new StringBuilder();
        MvcResult result;

        for (int i = 0; i < 192; i++) {
            sb.append("a");
        }
        registrationDTO.setLogin(sb.toString());

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.login");
    }

    @Test
    public void registerNewUser_EmailLongerThan191Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        StringBuilder sb = new StringBuilder();
        MvcResult result;

        for (int i = 0; i < 64; i++) {
            sb.append("a");
        }
        sb.append("@");
        for (int i = 0; i < 123; i++) {
            sb.append("b");
        }
        sb.append(".com");

        registrationDTO.setEmail(sb.toString());

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.email");
    }

    @Test
    public void registerNewUser_EmailLessThan5Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        registrationDTO.setEmail("a@b");

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.email");
    }

    @Test
    public void registerNewUser_LoginLessThan8Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        registrationDTO.setLogin("1234567");

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.login");
    }

    @Test
    public void registerNewUser_PasswordLessThan8Characters_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        registrationDTO.setPassword("1234567");

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.password");
    }

    @Test
    public void registerNewUser_FirstNameMoreThan255Character_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        StringBuilder sb = new StringBuilder();
        MvcResult result;

        for (int i = 0; i < 256; i++) {
            sb.append("a");
        }

        registrationDTO.setFirstName(sb.toString());

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.firstName");
    }

    @Test
    public void registerNewUser_LastNameMoreThan255Character_BadRequestReturned() throws Exception {
        UserRegistrationDTO registrationDTO = createSimpleUserRegistrationDTO();
        StringBuilder sb = new StringBuilder();
        MvcResult result;

        for (int i = 0; i < 256; i++) {
            sb.append("a");
        }

        registrationDTO.setLastName(sb.toString());

        result = registerNewUser(registrationDTO).andExpect(status().isBadRequest()).andReturn();

        checkReturnedMessageCode(result, "Length.userRegistrationDTO.lastName");
    }

    @Test
    public void registerNewUser_NotHtmlSafeInput_BadRequest() throws Exception {
        UserRegistrationDTO userRegistrationDTO = createSimpleUserRegistrationDTO();
        MvcResult result;

        userRegistrationDTO.setEmail("<script>alert('I can steal cookies')</script>@mail.com");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Email.userRegistrationDTO.email");

        userRegistrationDTO = createSimpleUserRegistrationDTO();
        userRegistrationDTO.setLogin("<script>alert('I can steal cookies')</script>");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "Pattern.userRegistrationDTO.login");

        userRegistrationDTO = createSimpleUserRegistrationDTO();
        userRegistrationDTO.setFirstName("<script>alert('I can steal cookies')</script>");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "SafeHtml.userRegistrationDTO.firstName");

        userRegistrationDTO = createSimpleUserRegistrationDTO();
        userRegistrationDTO.setLastName("<script>alert('I can steal cookies')</script>");
        result = registerNewUser(userRegistrationDTO).andExpect(status().isBadRequest()).andReturn();
        checkReturnedMessageCode(result, "SafeHtml.userRegistrationDTO.lastName");
    }

    @Test
    public void checkAvailableEmail_NotExistentEmail_OkReturnedWithAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setEmail("not@exist.com");

        MvcResult result = checkAvailableEmail(userFindDTO).andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "user.email.isAvailable");
    }

    @Test
    public void checkAvailableEmail_ExistentEmail_OkReturnedWithNotAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setEmail("user2@email.com");

        MvcResult result = checkAvailableEmail(userFindDTO).andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "user.email.isNotAvailable");
    }

    @Test
    public void checkAvailableEmail_NotExistentLogin_OkReturnedWithAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setLogin("notExist");

        MvcResult result = checkAvailableLogin(userFindDTO).andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "user.login.isAvailable");
    }

    @Test
    public void checkAvailableEmail_ExistentLogin_OkReturnedWithNotAvailableMessage() throws Exception {
        UserFindDTO userFindDTO = new UserFindDTO();

        userFindDTO.setLogin("user2");

        MvcResult result = checkAvailableLogin(userFindDTO).andExpect(status().isOk()).andReturn();

        checkReturnedMessageCode(result, "user.login.isNotAvailable");
    }

    private ResultActions registerNewUser(UserRegistrationDTO userRegistrationDTO) throws Exception {
        return mockMvc.perform(put("/api/registration/register-new-user")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userRegistrationDTO))
        );
    }

    private ResultActions checkAvailableEmail(UserFindDTO userFindDTO) throws Exception {
        return mockMvc.perform(post("/api/registration/check-available-email")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userFindDTO))
        );
    }

    private ResultActions checkAvailableLogin(UserFindDTO userFindDTO) throws Exception {
        return mockMvc.perform(post("/api/registration/check-available-login")
                        .contentType(MEDIA_TYPE_APPLICATION_JSON_UTF8)
                        .content(jacksonObjectMapper.writeValueAsString(userFindDTO))
        );
    }
}
