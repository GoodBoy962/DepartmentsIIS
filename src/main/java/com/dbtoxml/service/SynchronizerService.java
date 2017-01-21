package com.dbtoxml.service;

import com.dbtoxml.dao.DepartmentDao;
import com.dbtoxml.dao.impl.DepartmentDaoImpl;
import com.dbtoxml.exception.DepartmentFirstKeyEqualException;
import com.dbtoxml.model.Department;
import com.dbtoxml.model.DepartmentFirstKey;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aleksandrpliskin on 21.01.17.
 */
public class SynchronizerService {

    private static final Logger LOGGER = Logger.getLogger(SynchronizerService.class);

    public void synchronize(final String fileName) {
        try {
            Map<DepartmentFirstKey, String> departments = parseXmlFile(fileName);
            updateDataBaseDepartments(departments);
        } catch (IOException | JDOMException e) {
            LOGGER.error("Problems with reading from XML file", e);
        } catch (DepartmentFirstKeyEqualException e) {
            LOGGER.error("There two the same elements in given XML file", e);
        } catch (SQLException e) {
            LOGGER.error("Database wasn't updated", e);
        }
    }

    private Map<DepartmentFirstKey, String> parseXmlFile(final String fileName)
            throws JDOMException, IOException, DepartmentFirstKeyEqualException {

        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(fileName);
        Document document = builder.build(xmlFile);
        Element rootNode = document.getRootElement();
        List<Element> nodes = rootNode.getChildren("department");
        Map<DepartmentFirstKey, String> departments = new HashMap<>();

        for (Element node : nodes) {
            DepartmentFirstKey departmentFirstKey = new DepartmentFirstKey(
                    node.getAttributeValue("depCode"),
                    node.getAttributeValue("depJob"));
            String description = node.getAttributeValue("description");
            if (departments.containsKey(departmentFirstKey)) {
                throw new DepartmentFirstKeyEqualException();
            }
            departments.put(departmentFirstKey, description);
        }
        return departments;
    }

    private void updateDataBaseDepartments(Map<DepartmentFirstKey, String> departments)
            throws SQLException {

        DepartmentDao departmentDao = new DepartmentDaoImpl();
        List<Department> currentDepartments = departmentDao.getAll();
        List<Department> foundDepartments = new ArrayList<>();
        List<Department> newDepartments = new ArrayList<>();
        Map<DepartmentFirstKey, String> departmentsToUpdate = new HashMap<>();

        for (DepartmentFirstKey firstKey : departments.keySet()) {
            boolean contains = false;
            Department foundDepartment = null;
            for (Department department : currentDepartments) {
                if (department.getFirstKey().equals(firstKey)) {
                    contains = true;
                    foundDepartment = department;
                    foundDepartments.add(department);
                }
            }
            if (contains) {
                if (!foundDepartment.getDescription().equals(departments.get(firstKey))) {
                    departmentsToUpdate.put(firstKey, departments.get(firstKey));
                }
            } else {
                newDepartments.add(new Department(firstKey, departments.get(firstKey)));
            }
        }
        currentDepartments.removeAll(foundDepartments);
        List<DepartmentFirstKey> departmentsToDelete = currentDepartments.stream()
                .filter(department -> !foundDepartments.contains(department))
                .map(Department::getFirstKey).collect(Collectors.toList());

        departmentDao.update(newDepartments, departmentsToUpdate, departmentsToDelete);
    }
}