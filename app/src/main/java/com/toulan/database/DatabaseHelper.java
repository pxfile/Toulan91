package com.toulan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fast.library.database.BaseOrmLiteDatabaseHelper;
import com.toulan.bean.CallLogBean;
import com.toulan.bean.MyLocationBean;
import com.toulan.bean.SmsInfoBean;

import static org.greenrobot.eventbus.util.ErrorDialogManager.factory;

/**
 * 说明：DatabaseHelper
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:40
 * <p/>
 * 版本：verson 1.0
 */
public class DatabaseHelper extends BaseOrmLiteDatabaseHelper{

    private static DatabaseHelper helper;

    private DatabaseHelper(Context context) {
        super(context, "toulan.db", null, 1);
        addTable();
    }

    /**
     * 注册数据表
     */
    @Override
    public void addTable() {
        registerTable(MyLocationBean.class);
        registerTable(SmsInfoBean.class);
        registerTable(CallLogBean.class);
    }

    public static DatabaseHelper getInstance(Context context){
        if (helper == null){
            synchronized (DatabaseHelper.class){
                if (helper == null){
                    helper = new DatabaseHelper(context);
                }
            }
        }
        return helper;
    }

}
