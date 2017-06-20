package com.toulan.helper;

import com.fast.library.utils.ACache;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.NetUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.toulan.TApplication;
import com.toulan.bean.OrderBean;
import com.toulan.model.OnStringListener;
import com.toulan.model.TDataManager;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import org.json.JSONArray;
import java.util.ArrayList;

import static com.toulan.utils.Tsp.KEY.CACHE_TIME;
import static com.toulan.utils.Tsp.mCache;

/**
 * 说明：订单列表
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/20 15:35
 * <p/>
 * 版本：verson 1.0
 */
public class OrderHelper {

    public interface OnOrderListListener{
        void onStart();
        void onSuccess(ArrayList<OrderBean> orderList);
        void onFail(String msg);
        void onFinish();
    }

    private static String getCacheKey(String days){
        return days + Tsp.getUserInfo().getName();
    }

    private static String createQueryKey(String days){
        String result = "";
        try {
            JSONArray array = new JSONArray();
            array.put(days);
            result = array.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static void getOrderList(final String days, final OnOrderListListener listener){
        final ArrayList<OrderBean> orderList = new ArrayList<>();
        String cache = Tsp.mCache.getAsString(getCacheKey(days));
        if (!StringUtils.isEmpty(cache)){
            ArrayList<OrderBean> temp = GsonUtils.toList(GsonUtils.optString(cache,days),OrderBean.class);
            orderList.addAll(temp);
        }
        if (NetUtils.isNetConnected()){
            TDataManager.getInstance().orderList(createQueryKey(days), new OnStringListener() {
                @Override
                public void onStart(int what) {
                    if (listener != null){
                        listener.onStart();
                    }
                }
                @Override
                public void onFinish(int what) {
                    if (listener != null){
                        listener.onFinish();
                    }
                }
                @Override
                public void onSuccess(int what, String node) {
                    try {
                        ArrayList<OrderBean> beans = GsonUtils.toList(GsonUtils.optString(node,days),OrderBean.class);
                        orderList.clear();
                        orderList.addAll(beans);
                        if (listener != null){
                            listener.onSuccess(orderList);
                        }
                        mCache.put(getCacheKey(days),node,Tsp.KEY.CACHE_TIME);
                    }catch (Exception e){
                        onFail(what,-1,"数据异常");
                    }
                }

                @Override
                public void onFail(int what, int code, String msg) {
                    if (listener != null){
                        listener.onFail(msg);
                    }
                }
            });
        }else {
            if (listener != null){
                if (orderList.isEmpty()){
                    listener.onFail(UIUtils.getString(R.string.network_error));
                }else {
                    listener.onSuccess(orderList);
                }
            }
        }
    }

}
