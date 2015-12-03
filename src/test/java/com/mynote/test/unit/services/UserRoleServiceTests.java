package com.mynote.test.unit.services;

import com.google.common.collect.Lists;
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

import static com.mynote.test.utils.UserRoleTestUtils.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
    public void addUserRole_NotExistentRole_RoleSavedInDB() throws UserRoleAlreadyExists {
        UserRole userRole = getNotExistentRole();

        userRole = userRoleService.addRole(userRole);

        assertNotNull(userRole.getId());
    }

    @Test(expected = UserRoleAlreadyExists.class)
    public void addUserRole_ExistentRole_ExceptionThrown() throws UserRoleAlreadyExists {
        UserRole userRole = getRoleUser();

        userRoleService.addRole(userRole);
    }

    @Test
    public void getAdminRole_AdminRoleReturned() {
        UserRole userRole = userRoleService.getRoleAdmin();

        assertThat(userRole, is(getRoleAdmin()));
    }

    @Test
    public void getAllUserRoles_ListOfRolesReturned() {
        List<UserRole> userRoleList = userRoleService.getAllUserRoles();

        assertThat(userRoleList, hasSize(2));

        assertThat(userRoleList, hasItem(getRoleAdmin()));
        assertThat(userRoleList, hasItem(getRoleUser()));
    }

    @Test
    public void findRoles_RoleWithIdNull_UserRolesUpdated() throws UserRoleNotFoundException {
        List<UserRole> noIdRoles = Lists.newArrayList(getRoleUser(), getRoleAdmin());

        Set<UserRole> userRoles = userRoleService.findRoles(noIdRoles);

        assertThat(userRoles, hasSize(2));
    }

    @Test(expected = UserRoleNotFoundException.class)
    public void findRoles_NotExistentRole_ExceptionThrown() throws UserRoleNotFoundException {
        List<UserRole> noIdRoles = Lists.newArrayList(getNotExistentRole());

        userRoleService.findRoles(noIdRoles);
    }

    @Test
    public void initializeRoleWithUsers_RoleWithoutInitializedUsersSet_UsersSetInitialized() {
        UserRole roleAdmin = userRoleService.getRoleAdmin();

        userRoleService.initializeUsersSet(roleAdmin);

        assertNotNull(roleAdmin.getUsers());
    }
}
