package com.dbtoxml.service;

import com.dbtoxml.dao.DepartmentDao;
import com.dbtoxml.dao.impl.DepartmentDaoImpl;
import com.dbtoxml.model.Department;
import com.dbtoxml.util.XmlFileWriter;

import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

/**
 * @author aleksandrpliskin on 21.01.17.
 */
public class XmlWriterService {

    private static final Logger LOGGER = Logger.getLogger(XmlWriterService.class);

    public void write(String fileName) {
        try {
            DepartmentDao departmentDao = new DepartmentDaoImpl();
            List<Department> departments = departmentDao.getAll();
            XmlFileWriter.write(fileName, departments);
            LOGGER.debug("The file " + fileName + " is ready");
        } catch (SQLException e) {
            LOGGER.error("Problems with selecting departments from DB", e);
        }
    }
}