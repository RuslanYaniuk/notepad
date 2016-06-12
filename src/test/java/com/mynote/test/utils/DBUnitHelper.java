package com.mynote.test.utils;

import com.mynote.test.utils.yaml.YamlDataSetLoader;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
@Component
public class DBUnitHelper {

    public static final String DBUNIT_DATASETS = "/dbunit-datasets/";

    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection dbUnitCon;

    private IDataSet users;
    private IDataSet roles;
    private IDataSet usersToRoles;

    @PostConstruct
    private void init() throws DatabaseUnitException, IOException {
        Connection con = DataSourceUtils.getConnection(dataSource);

        dbUnitCon = new DatabaseConnection(con);
        dbUnitCon.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

        users = YamlDataSetLoader.load(DBUNIT_DATASETS + "users.yml");
        roles = YamlDataSetLoader.load(DBUNIT_DATASETS + "roles.yml");
        usersToRoles = YamlDataSetLoader.load(DBUNIT_DATASETS + "users-to-roles.yml");
    }

    public void insertUsers() throws FileNotFoundException, SQLException, DatabaseUnitException {
        DatabaseOperation.INSERT.execute(dbUnitCon, users);
    }

    public void insertRoles() throws DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(dbUnitCon, roles);
    }

    public void insertUsersToRoles() throws DatabaseUnitException, SQLException {
        DatabaseOperation.INSERT.execute(dbUnitCon, usersToRoles);
    }

    public void deleteAllFixtures() throws DatabaseUnitException, SQLException {
        DatabaseOperation.DELETE_ALL.execute(dbUnitCon, usersToRoles);
        DatabaseOperation.DELETE_ALL.execute(dbUnitCon, roles);
        DatabaseOperation.DELETE_ALL.execute(dbUnitCon, users);
    }
}
