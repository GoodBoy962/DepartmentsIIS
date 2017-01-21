package com.dbtoxml.exception;

/**
 * @author aleksandrpliskin on 21.01.17.
 */
public class DepartmentFirstKeyEqualException extends Exception {

    public DepartmentFirstKeyEqualException() {
        this("There two equal elements in xml");
    }

    public DepartmentFirstKeyEqualException(String message) {
        super(message);
    }
}
