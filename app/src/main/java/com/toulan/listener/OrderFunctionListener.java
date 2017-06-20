package com.toulan.listener;

import com.toulan.bean.OrderBean;
import com.toulan.ui.adapter.TodayOrderAdapter;

/**
 * 说明：OrderFunctionListener
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/22 0:43
 * <p/>
 * 版本：verson 1.0
 */
public interface OrderFunctionListener {
    void clickOrderFunction(TodayOrderAdapter.Function function, OrderBean bean);
}
