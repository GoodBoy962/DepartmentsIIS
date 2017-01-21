package com.dbtoxml.dao;

import com.dbtoxml.model.Department;
import com.dbtoxml.model.DepartmentFirstKey;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author aleksandrpliskin on 20.01.17.
 */
public interface DepartmentDao {

    String SELECT_ALL_QUERY = "SELECT dep_code, dep_job, description FROM departments";
    String UPDATE_ONE_PREPARED_QUERY = "UPDATE departments SET description = ?WHERE dep_job = ? AND dep_code = ?";
    String DELETE_ONE_PREPARED_QUERY = "DELETE FROM departments WHERE dep_code = ? AND dep_job = ?";
    String UPDATE_ONE_PREPARED_STATEMENT = "INSERT INTO departments (dep_code, dep_job, description) VALUES (?, ?, ?)";

    List<Department> getAll() throws SQLException;

    void updateDescriptionByFirstKey(DepartmentFirstKey firstKey, String description) throws SQLException;

    void deleteByFirstKey(DepartmentFirstKey firstKey) throws SQLException;

    void save(Department department) throws SQLException;

    void update(List<Department> newDepartments,
                Map<DepartmentFirstKey, String> departmentsToUpdate,
                List<DepartmentFirstKey> departmentsToDelete) throws SQLException;
}
