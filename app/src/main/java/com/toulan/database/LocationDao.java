package com.toulan.database;

import android.content.Context;

import com.fast.library.database.BaseOrmLiteDao;
import com.j256.ormlite.dao.Dao;
import com.toulan.bean.MyLocationBean;

/**
 * 说明：LocationDao
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:41
 * <p/>
 * 版本：verson 1.0
 */
public class LocationDao extends BaseOrmLiteDao<MyLocationBean,Integer>{
    public LocationDao(Context context) {
        super(context);
    }

    @Override
    public Dao<MyLocationBean, Integer> getOrmLiteDao(Context context) {
        return DatabaseHelper.getInstance(context).getDao(MyLocationBean.class);
    }
}
