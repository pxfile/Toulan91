package com.fast.library.database;

import java.util.List;

/**
 * 说明：集合中是否存在指定元素
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/6/7 22:27
 * <p/>
 * 版本：verson 1.0
 */
public class CollectionUtil {

    public static boolean existValue(String value, List<String> list) {
        if (list == null || value == null) {
            return false;
        }
        for (String str : list) {
            if (value.equals(str)) {
                return true;
            }
        }
        return false;
    }

}
