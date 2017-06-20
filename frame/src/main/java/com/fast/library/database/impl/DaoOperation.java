package com.fast.library.database.impl;

/**
 * 说明：数据库操作类型
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/6/7 21:49
 * <p/>
 * 版本：verson 1.0
 */
public interface DaoOperation {

    int INSERT = 1;
    int DELETE = 2;
    int UPDATE = 3;
    int SELECT = 4;
    int INSERT_BATCH = 5;
    int DELETE_BATCH = 6;
    int UPDATE_BATCH = 7;
}
