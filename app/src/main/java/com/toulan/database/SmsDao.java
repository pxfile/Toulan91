package com.toulan.database;

import android.content.Context;

import com.fast.library.database.BaseOrmLiteDao;
import com.j256.ormlite.dao.Dao;
import com.toulan.bean.SmsInfoBean;

/**
 * 说明：SmsDao
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:41
 * <p/>
 * 版本：verson 1.0
 */
public class SmsDao extends BaseOrmLiteDao<SmsInfoBean,Integer>{
    public SmsDao(Context context) {
        super(context);
    }

    @Override
    public Dao<SmsInfoBean, Integer> getOrmLiteDao(Context context) {
        return DatabaseHelper.getInstance(context).getDao(SmsInfoBean.class);
    }
}
