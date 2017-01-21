package com.dbtoxml.model;

/**
 * @author aleksandrpliskin on 20.01.17.
 */
public class Department {

    private String depCode;

    private String depJob;

    private String description;

    public Department() {
    }

    public Department(String depCode, String depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    public Department(DepartmentFirstKey firstKey, String description) {
        this(firstKey.getDepCode(), firstKey.getDepJob(), description);
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    public void setDepJob(String depJob) {
        this.depJob = depJob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DepartmentFirstKey getFirstKey() {
        return new DepartmentFirstKey(depCode, depJob);
    }
}
