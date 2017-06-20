package com.toulan.bean;

import com.fast.library.utils.DateUtils;

/**
 * 说明：工作标签
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/18 12:54
 * <p/>
 * 版本：verson 1.0
 */
public class JobTabBean {
    public int index;
//    星期【周一】
    public String weekDay;
//    日期【6-23】
    public String data;
//    毫秒格式【10000000000】
    public long dataMills;

    public String getQueryKey(){
        return DateUtils.getLongToStr(dataMills,DateUtils.FORMAT_YYYY_MM_DD_1);
    }
}
