package com.mynote.test.unit.services;

import com.mynote.dto.user.UserRoleDTO;
import com.mynote.exceptions.UserRoleAlreadyExists;
import com.mynote.exceptions.UserRoleNotFoundException;
import com.mynote.models.UserRole;
import com.mynote.services.UserRoleService;
import com.mynote.test.utils.DBUnitHelper;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class UserRoleServiceTests extends AbstractServiceTest {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DBUnitHelper dbUnitHelper;

    @Before
    public void setup() throws DatabaseUnitException, SQLException, FileNotFoundException {
        dbUnitHelper.deleteUsersFromDb();
        dbUnitHelper.cleanInsertUsersIntoDb();
    }

    @Test
    public void getAdminRole_AdminRoleReturned() {
        UserRole userRole = userRoleService.getRoleAdmin();

        assertThat(userRole.getRole(), is(UserRoleService.ROLE_ADMIN));

        userRole = userRoleService.getRoleUser();
        assertThat(userRole.getRole(), is(UserRoleService.ROLE_USER));
    }

    @Test
    public void getAllUserRoles_ListOfRolesReturned() {
        List<UserRole> userRoleList = userRoleService.getAllUserRoles();

        assertThat(userRoleList.size(), is(2));

        assertTrue(userRoleList.contains(new UserRole("ROLE_USER")));
        assertTrue(userRoleList.contains(new UserRole("ROLE_ADMIN")));
    }

    @Test
    public void findRoles_CorrectUserRolesDTOArray_UserRolesUpdated() throws UserRoleNotFoundException {
        UserRoleDTO[] userRoleDTOs = new UserRoleDTO[2];

        userRoleDTOs[0] = new UserRoleDTO(1L, "ROLE_USER");
        userRoleDTOs[1] = new UserRoleDTO(2L, "ROLE_ADMIN");

        Set<UserRole> userRoles = userRoleService.findRoles(userRoleDTOs);

        assertThat(userRoles.size(), is(userRoleDTOs.length));
    }

    @Test(expected = UserRoleNotFoundException.class)
    public void findRoles_NotExistentRole_ExceptionThrown() throws UserRoleNotFoundException {
        UserRoleDTO[] userRoleDTOs = new UserRoleDTO[2];

        userRoleDTOs[0] = new UserRoleDTO(1L, "ROLE_USER");
        userRoleDTOs[1] = new UserRoleDTO(2L, "ROLE_ADMIN");
        userRoleDTOs[1] = new UserRoleDTO(-456L, "ROLE_WHO");

        userRoleService.findRoles(userRoleDTOs);
    }

    @Test
    public void addUserRole_NotExistentRole_RoleSavedInDB() throws UserRoleAlreadyExists {
        UserRoleDTO userRoleDTO = new UserRoleDTO();

        userRoleDTO.setRole("ROLE_MANAGER");
        assertNotNull(userRoleService.addRole(userRoleDTO));
    }

    @Test(expected = UserRoleAlreadyExists.class)
    public void addUserRole_ExistentRole_ExceptionThrown() throws UserRoleAlreadyExists {
        UserRoleDTO userRoleDTO = new UserRoleDTO();

        userRoleDTO.setRole("ROLE_USER");
        userRoleService.addRole(userRoleDTO);
    }

    @Test
    public void initializeRoleWithUsers_RoleWithoutInitializedUsersSet_UsersSetInitialized() {
        UserRole roleAdmin = userRoleService.getRoleAdmin();

        userRoleService.initializeUsersSet(roleAdmin);

        assertNotNull(roleAdmin.getUsers());
    }
}
