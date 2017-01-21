package com.dbtoxml.dao.impl;

import com.dbtoxml.dao.DepartmentDao;
import com.dbtoxml.model.Department;
import com.dbtoxml.model.DepartmentFirstKey;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author aleksandrpliskin on 20.01.17.
 */
public class DepartmentDaoImpl implements DepartmentDao {

    private static final Logger LOGGER = Logger.getLogger(DepartmentDaoImpl.class);

    private static Connection conn;

    static {
        String driver = null;
        String url = null;
        String username = null;
        String password = null;

        try (InputStream in = DepartmentDaoImpl.class
                .getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            driver = properties.getProperty("driver");
        } catch (IOException e) {
            LOGGER.error("Failed to load properties!", e);
        }
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Failed to get connection to the db", e);
        }
    }

    @Override
    public List<Department> getAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(SELECT_ALL_QUERY);
        LOGGER.debug("Department entities were taken from DB");
        while (rs.next()) {
            Department department = new Department();
            department.setDepCode(rs.getString("dep_code"));
            department.setDepJob(rs.getString("dep_job"));
            department.setDescription(rs.getString("description"));
            departments.add(department);
        }
        return departments;
    }

    @Override
    public void updateDescriptionByFirstKey(DepartmentFirstKey firstKey, String description)
            throws SQLException {
        PreparedStatement statement =
                conn.prepareStatement(UPDATE_ONE_PREPARED_QUERY);
        statement.setString(1, description);
        statement.setString(2, firstKey.getDepJob());
        statement.setString(3, firstKey.getDepCode());
        statement.execute();
    }

    @Override
    public void deleteByFirstKey(DepartmentFirstKey firstKey) throws SQLException {
        PreparedStatement statement =
                conn.prepareStatement(DELETE_ONE_PREPARED_QUERY);
        statement.setString(1, firstKey.getDepCode());
        statement.setString(2, firstKey.getDepJob());
        statement.execute();
    }

    @Override
    public void save(Department department) throws SQLException {
        PreparedStatement statement =
                conn.prepareStatement(UPDATE_ONE_PREPARED_STATEMENT);
        statement.setString(1, department.getDepCode());
        statement.setString(2, department.getDepJob());
        statement.setString(3, department.getDescription());
        statement.execute();
    }

    @Override
    public void update(List<Department> newDepartments,
                       Map<DepartmentFirstKey, String> departmentsToUpdate,
                       List<DepartmentFirstKey> departmentsToDelete) throws SQLException {
        conn.setAutoCommit(false);
        LOGGER.debug("transaction started");
        LOGGER.debug("saving new departments started");
        for (Department department : newDepartments) {
            save(department);
        }
        LOGGER.debug("new departments were saved");
        LOGGER.debug("updating departments were started");
        for (DepartmentFirstKey firstKey : departmentsToUpdate.keySet()) {
            updateDescriptionByFirstKey(firstKey, departmentsToUpdate.get(firstKey));
        }
        LOGGER.debug("departments were updated");
        LOGGER.debug("deleting departments started");
        for (DepartmentFirstKey firstKey : departmentsToDelete) {
            deleteByFirstKey(firstKey);
        }
        LOGGER.debug("departments were deleted");
        conn.commit();
        LOGGER.debug("transaction was successfully commited");
    }
}