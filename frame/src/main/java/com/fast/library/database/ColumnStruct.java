package com.fast.library.database;

/**
 * 说明：数据库表中列的结构
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/6/7 22:14
 * <p/>
 * 版本：verson 1.0
 */
public class ColumnStruct {
    public String columnName;
    public String columnLimit;

    public ColumnStruct() {
    }

    public ColumnStruct(String columnName, String columnLimit) {
        this.columnName = columnName;
        this.columnLimit = columnLimit;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnLimit() {
        return columnLimit;
    }

    public void setColumnLimit(String columnLimit) {
        this.columnLimit = columnLimit;
    }

    @Override
    public String toString() {
        return "ColumnStruct{" +
                "columnName='" + columnName + '\'' +
                ", columnLimit='" + columnLimit + '\'' +
                '}';
    }
}
