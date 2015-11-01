package com.mynote.test.utils.db;

import com.mynote.test.utils.yaml.YamlDataSetLoader;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
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

    @Autowired
    private DataSource dataSource;

    private IDatabaseConnection dbUnitCon;

    private IDataSet userDataSet;

    private IDataSet usersHasRolesDataSet;

    @PostConstruct
    private void init() throws DatabaseUnitException, IOException {
        Connection con = DataSourceUtils.getConnection(dataSource);

        dbUnitCon = new DatabaseConnection(con);

        userDataSet = YamlDataSetLoader.load("/dbunit-datasets/users-and-roles.yml");
        usersHasRolesDataSet = YamlDataSetLoader.load("/dbunit-datasets/users-to-roles.yml");
    }

    public void cleanInsertUsersIntoDb() throws FileNotFoundException, SQLException, DatabaseUnitException {
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, userDataSet);
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitCon, usersHasRolesDataSet);
    }

    public void deleteUsersFromDb() throws DatabaseUnitException, SQLException {
        DatabaseOperation.DELETE_ALL.execute(dbUnitCon, usersHasRolesDataSet);
        DatabaseOperation.DELETE_ALL.execute(dbUnitCon, userDataSet);
    }

    public IDataSet getUserDataSet() {
        return userDataSet;
    }
}
