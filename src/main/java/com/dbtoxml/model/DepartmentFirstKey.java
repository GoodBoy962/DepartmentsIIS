package com.dbtoxml.model;

/**
 * @author aleksandrpliskin on 21.01.17.
 */
public class DepartmentFirstKey {

    private String depCode;
    private String depJob;

    public DepartmentFirstKey() {
    }

    public DepartmentFirstKey(String depCode, String depJob) {
        this.depCode = depCode;
        this.depJob = depJob;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentFirstKey)) return false;
        DepartmentFirstKey that = (DepartmentFirstKey) o;
        return (depCode != null ? depCode.equals(that.getDepCode()) : that.getDepCode() == null)
                && (depJob != null ? depJob.equals(that.getDepJob()) : that.getDepJob() == null);
    }

    @Override
    public int hashCode() {
        int result = depCode != null ? depCode.hashCode() : 0;
        result = 31 * result + (depJob != null ? depJob.hashCode() : 0);
        return result;
    }
}