package com.toulan.utils;

import com.fast.library.utils.SDCardUtils;
import com.fast.library.utils.UIUtils;
import com.toulan91.R;

import java.io.File;

/**
 * 说明：常量
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 9:22
 * <p/>
 * 版本：verson 1.0
 */
public class TConstant {
    //应用根目录
    public final static String APP = "Toulan91";
    //崩溃日志保存路径
    public final static String CRASH = SDCardUtils.getExternalStorage() + File.separator + APP + File.separator + "crash" + File.separator;
    //超时时间
    public final static int TIME_OUT = 5000;
    //超时时间
    public final static int ORDER_TIMER_30_MIN_VIB_TIME = 1500;
    //应用升级缓存文件夹
    public final static String APP_UPDATE_FILE = SDCardUtils.getExternalStorage() + File.separator + APP + File.separator + "app" + File.separator;
    //上传短信和通化记录的Action
    public final static String ACTION_UPLOAD = "com.toulan.action.upload";
    public final static String ACTION_UPLOAD_PARAMS = "params";
    public final static String ACTION_UPLOAD_SMS = "sms";
    public final static String ACTION_UPLOAD_CALL = "call";

    public final static String OPENIM_APP_KEY = "23463438";
    public interface BaiDuSpeak{
        String API_KEY = "WD0brA95HZa8mPzIjfduwUif";
        String SECRET_KEY = "ed8e37bd8b70f1e89d1296fd7b068e51";
        String APP_ID = "8622156";
    }

    public interface BaiDuTrace{
        long TRACE_SERVICE_ID = 127170;
        long Radius = 100;
        int gatherInterval = 10;//采集周期（单位 : 秒）
        int packInterval = 20;//设置打包周期(单位 : 秒)
    }

    /**
     * 网络请求方法
     */
    public static class Method{
        private static String getMethod(String method){
            return UIUtils.getString(R.string.url) + method;
        }
//        获取服务器时间
        public final static String serverTime = getMethod("get-time");
        public final static int WHAT_SERVERTIME = 10001;
//        短信验证
        public final static String sms = getMethod("get-sms");
        public final static int WHAT_SMS = 10002;
//        登录
        public final static String login = getMethod("login");
        public final static int WHAT_LOGIN = 10003;
//        登录
        public final static String orderList = getMethod("order-list");
        public final static int WHAT_ORDERLIST = 10004;
//        更多
        public final static String moreUrl = getMethod("more-url");
        public final static int WHAT_MOREURL = 10005;
//        拨打电话
        public final static String callPhone = getMethod("private-line");
        public final static int WHAT_CALLPHONE = 10006;
//        定位
        public final static String location = getMethod("position");
        public final static int WHAT_LOCATION = 10007;
//        确认订单
        public final static String ORDER_SAVE = getMethod("order-post-save");
        public final static int WHAT_ORDER_SAVE = 10008;
//        上传图片凭证
        public final static String UPLOAD_TOKEN = getMethod("upload-file");
        public final static int WHAT_UPLOAD_TOKEN = 10009;
        public final static String UPLOAD_FILE = getMethod("upload-file");
        public final static int WHAT_UPLOAD_FILE = 10010;
//        app检查更新
        public final static String CHECK_APP = getMethod("app-version");
        public final static int WHAT_CHECK_APP = 10011;
//        通话记录上传
        public final static String UPLOAD_CALL = getMethod("app-call");
//        短信记录上传
        public final static String UPLOAD_SMS = getMethod("app-sms");
//        百度鹰眼围栏报警
        public final static String UPLOAD_FENCE = getMethod("baidu-circular-fence");
        public final static int WHAT_UPLOAD_FENCE = 10012;
    }

    public static class EventType{
//        点击某一天刷新
        public final static String toRefreshJobOrder = "toRefreshJobOrder";
        public final static String toOrderDetailPic = "toOrderDetailPic";
        public final static String mainShowMessageNumber = "mainShowMessageNumber";
        public final static String createCircularFence = "createCircularFence";
    }

    public static class Postion{
        public final static int UPDATE_SIZE = 6;
        public final static int POSITION_ID = 10000;
        public final static int LOCATION_TIME_SPAN = 10 * 60 * 1000;//每10分钟定位
        public final static String LOCATION_START = "06:30";//开始定位时间
        public final static String LOCATION_END = "22:00";//结束定位时间
    }

    public static class OrderTimerSpeech{
        public final static String START_SPEECH = "尊敬的用户您好，工号（%s）开始为您服务，请您检查他是否出示工牌，并交代清洁重点，收好贵重物品。";//order_timer_start
        public final static String PAUSE_SPEECH = "工号（%s），您的服务计时已暂停，如需激活请点击开始。";//order_timer_pause
        public final static String PAUSE_TO_START_SPEECH = "工号（%s）,您的服务计时已继续，请专心干活。";//暂停又开始
        public final static String MIN_30_SPEECH = "工号（%s）,您剩余服务时长不足30分钟，要加油哟！记得邀请客户检查工作，并对需要返工的地方认真收尾。";//30分钟
        public final static String STOP_SPEECH = "工号（%s）,您的服务时间已到，记得，一，叮嘱客户检查物品是否有丢失，二，让客户微信扫码点评或签纸质确认单，三，不拿客户一针一线，临走前礼貌说再见！";//倒计时结束
        public final static String ADD_TIME_SPEECH = "工号（%s）,加时服务一定要事先告知客户加时收费标准，并征得客户同意后方可提供加时服务！";//order_timer_add
        public final static String CLICK_ADD_TIME_SPEECH = "工号（%s）,您已进入加时服务阶段！";//确认加时
        public final static String CONFIRM_SPEECH = "工号（%1$s）,您的服务时间为(%2$s)，记得，一，叮嘱客户检查物品是否有丢失，二，让客户微信扫码点评或签纸质确认单，三，不拿客户一针一线，临走前礼貌说再见！";//加时完成
    }


}