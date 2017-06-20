package com.toulan.database;

import android.content.Context;

import com.fast.library.database.BaseOrmLiteDao;
import com.j256.ormlite.dao.Dao;
import com.toulan.bean.CallLogBean;

/**
 * 说明：CallDao
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:41
 * <p/>
 * 版本：verson 1.0
 */
public class CallDao extends BaseOrmLiteDao<CallLogBean,Integer>{
    public CallDao(Context context) {
        super(context);
    }

    @Override
    public Dao<CallLogBean, Integer> getOrmLiteDao(Context context) {
        return DatabaseHelper.getInstance(context).getDao(CallLogBean.class);
    }
}
