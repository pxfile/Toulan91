package com.toulan.openIM;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListUI;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWCustomConversationBody;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.mobileim.lib.presenter.conversation.CustomViewConversation;
import com.alibaba.mobileim.utility.IMSmilyCache;
import com.toulan91.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：ConversationListUI
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/27 22:38
 * <p/>
 * 版本：verson 1.0
 */
public class ConversationListUI extends IMConversationListUI{

    public static final String SYSTEM_TRIBE_CONVERSATION="sysTribe";
    public static final String SYSTEM_FRIEND_REQ_CONVERSATION="sysfrdreq";

    public ConversationListUI(Pointcut pointcut) {
        super(pointcut);
    }

    @Override
    public boolean needHideTitleView(Fragment fragment) {
        return true;
    }

    @Override
    public View getCustomEmptyViewInConversationUI(Context context) {
        TextView textView = new TextView(context);
        textView.setText("还没有会话哦，快去找人聊聊吧!");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        return textView;
    }

    /**
     * 获取自定义会话要展示的自定义View，该方法的实现类完全似于BaseAdapter中getView()方法实现
     * @param context
     * @param conversation
     *          自定义展示View的会话
     * @param convertView
     *          {@link android.widget.BaseAdapter#getView(int, View, ViewGroup)}的第二个参数
     * @param parent
     *          {@link android.widget.BaseAdapter#getView(int, View, ViewGroup)}的第三个参数
     * @return
     */
    @Override
    public View getCustomView(Context context, YWConversation conversation, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder = null;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.demo_conversation_custom_view_item,
                    parent,
                    false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.conversationName = (TextView)convertView.findViewById(R.id.conversation_name);
        viewHolder.conversationContent = (TextView)convertView.findViewById(R.id.conversation_content);
        CustomViewConversation customViewConversation = (CustomViewConversation)conversation;
        if(TextUtils.isEmpty(customViewConversation.getConversationName()))
            viewHolder.conversationName.setText("可自定义View布局的会话");
        else
            viewHolder.conversationName.setText(customViewConversation.getConversationName());
        viewHolder.conversationContent.setText(customViewConversation.getLatestContent());
        return convertView;
    }

    static class ViewHolder {
        TextView conversationName;
        TextView conversationContent;
    }

    /**
     * 会话列表onDestroy事件
     * @param fragment
     */
    @Override
    public void onDestroy(Fragment fragment) {
        super.onDestroy(fragment);
    }

    /**
     * 会话列表Activity创建事件
     * @param savedInstanceState
     * @param fragment
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState, Fragment fragment) {
        super.onActivityCreated(savedInstanceState, fragment);
    }

    /**
     * 会话列表onResume事件
     * @param fragment
     */
    @Override
    public void onResume(Fragment fragment) {
        super.onResume(fragment);
    }

    /**
     * 返回设置最近联系人界面背景的资源Id,返回0则使用默认值
     * @return
     *      资源Id
     */
    @Override
    public int getCustomBackgroundResId() {
        return 0;
    }

    /*********** 以下是定制会话item view的示例代码 ***********/
    //有几种自定义，数组元素就需要几个，数组元素值从0开始
    //private final int[] viewTypeArray = {0,1,2,3}，这样就有4种自定义View
    private final int[] viewTypeArray = {0};
    /**
     * 自定义item view的种类数
     * @return 种类数
     */
    @Override
    public int getCustomItemViewTypeCount() {
        return viewTypeArray.length;
    }

    /**
     * 自定义item的viewType
     * @param conversation
     * @return
     */
    @Override
    public int getCustomItemViewType(YWConversation conversation) {
        //todo 若修改 YWConversationType.Tribe为自己type，SDK认为您要在｛@link #getCustomItemView｝中完全自定义，针对群的自定义，如getTribeConversationHead会失效。
        //todo 该原则同样适用于 YWConversationType.P2P等其它内部类型，请知晓！
        if (conversation.getConversationType() == YWConversationType.Custom) {
            return viewTypeArray[0];
        }
//        else if (conversation.getConversationType() == YWConversationType.P2P){
//            return viewTypeArray[1];
//        }
        //这里必须调用基类方法返回！！
        return super.getCustomItemViewType(conversation);
    }



    /**
     * 根据viewType自定义item的view
     * @param fragment
     * @param conversation      当前item对应的会话
     * @param convertView       convertView
     * @param viewType          当前itemView的viewType
     * @param headLoadHelper    加载头像管理器，用户可以使用该管理器设置头像
     * @param parent            getView中的ViewGroup参数
     * @return
     */
    @Override
    public View getCustomItemView(Fragment fragment, YWConversation conversation, View convertView, int viewType, YWContactHeadLoadHelper headLoadHelper, ViewGroup parent) {
        if (viewType == viewTypeArray[0]){
            ViewHolder1 holder = null;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
                holder = new ViewHolder1();
                convertView = inflater.inflate(R.layout.demo_custom_conversation_item_1, parent, false);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.unread = (TextView) convertView.findViewById(R.id.unread);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }

            String name = "";
            if (conversation.getConversationBody() instanceof YWCustomConversationBody) {
                YWCustomConversationBody body = (YWCustomConversationBody) conversation.getConversationBody();
                String conversationId = body.getIdentity();
                if(conversationId.equals(SYSTEM_TRIBE_CONVERSATION)){
                    headLoadHelper.setTribeDefaultHeadView(holder.head);
                    name = "群系统消息";
                }else  if(conversationId.equals(SYSTEM_FRIEND_REQ_CONVERSATION)){
                    headLoadHelper.setDefaultHeadView(holder.head);
                    name = "联系人系统消息";
                }else{
                    headLoadHelper.setDefaultHeadView(holder.head);
                    name = "这是一个自定义会话";
                }
            }
            headLoadHelper.setHeadView(holder.head, conversation);
            holder.name.setText(name);

            holder.unread.setVisibility(View.GONE);
            int unreadCount = conversation.getUnreadCount();
            if (unreadCount > 0) {
                holder.unread.setVisibility(View.VISIBLE);
                if (unreadCount > 99){
                    holder.unread.setText("99+");
                }else {
                    holder.unread.setText(String.valueOf(unreadCount));
                }
            }

            return convertView;
        }
//        else if (viewType == viewTypeArray[1]){
//            return super.getCustomItemView(fragment, conversation, convertView, viewType, headLoadHelper, parent);
//        }
//        else if (viewType == viewTypeArray[1]){
//            ViewHolder2 holder = null;
//            if (convertView == null){
//                LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
//                holder = new ViewHolder2();
//                convertView = inflater.inflate(R.layout.demo_custom_conversation_item_2, parent, false);
//                holder.head = (ImageView) convertView.findViewById(R.id.head);
//                holder.unread = (TextView) convertView.findViewById(R.id.unread);
//                holder.name = (TextView) convertView.findViewById(R.id.name);
//                holder.content = (TextView) convertView.findViewById(R.id.content);
//                holder.atMsgNotify = (TextView) convertView.findViewById(R.id.at_msg_notify);
//                holder.draftNotify = (TextView) convertView.findViewById(R.id.at_msg_notify);
//                holder.time = (TextView) convertView.findViewById(R.id.time);
//                convertView.setTag(holder);
//                YWLog.i(TAG, "convertView == null");
//            } else {
//                holder = (ViewHolder2)convertView.getTag();
//                YWLog.i(TAG, "convertView != null");
//            }
//
//            holder.unread.setVisibility(View.GONE);
//            int unreadCount = conversation.getUnreadCount();
//            if (unreadCount > 0) {
//                holder.unread.setVisibility(View.VISIBLE);
//                if (unreadCount > 99){
//                    holder.unread.setText("99+");
//                }else {
//                    holder.unread.setText(String.valueOf(unreadCount));
//                }
//            }
//
//            YWTribeConversationBody body = (YWTribeConversationBody) conversation.getConversationBody();
//            YWTribe tribe = body.getTribe();
//            String name = tribe.getTribeName();
//            holder.name.setText(name);
//
//            YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
//            YWContactHeadLoadHelper helper = new YWContactHeadLoadHelper(fragment.getActivity(), null);
//            /**
//             * !!!注:这里是给群回话设置头像,这里直接设置的默认头像
//             * 如果想自由设置可以使用{@link YWContactHeadLoadHelper#setCustomHeadView(ImageView, int, String)},
//             * 该方法的第三个参数为加载头像的地址:可以是资源Id或者是sdcard上的file绝对路径以及网络url
//             */
//            if (tribe.getTribeType() == YWTribeType.CHATTING_TRIBE) {
//                helper.setTribeDefaultHeadView(holder.head);
//            } else {
//                helper.setRoomDefaultHeadView(holder.head);
//            }
//
//            //是否支持群@消息提醒
//            boolean isAtEnalbe = YWAPI.getYWSDKGlobalConfig().enableTheTribeAtRelatedCharacteristic();
//            if (isAtEnalbe){
//                //文案修改为@消息提醒
//                holder.atMsgNotify.setText(R.string.aliwx_at_msg_notify);
//                if (conversation.hasUnreadAtMsg()) {
//                    holder.atMsgNotify.setVisibility(View.VISIBLE);
//                } else {
//                    holder.atMsgNotify.setVisibility(View.GONE);
//                }
//            } else {
//                holder.atMsgNotify.setVisibility(View.GONE);
//            }
//            String content = conversation.getLatestContent();
//            boolean isDraftEnable = YWAPI.getYWSDKGlobalConfig().enableConversationDraft();
//            //没有开启@消息开关或者@消息提醒不可见,说明此时没有@消息,检查是否有草稿
//            if(isDraftEnable) {
//                if (!isAtEnalbe || (holder.atMsgNotify != null && holder.atMsgNotify.getVisibility() != View.VISIBLE)) {
//                    if (conversation.getConversationDraft() != null
//                            && !TextUtils.isEmpty(conversation.getConversationDraft().getContent())) {
//                        //文案修改为草稿提醒
//                        holder.draftNotify.setText(R.string.aliwx_draft_notify);
//                        content = conversation.getConversationDraft().getContent();
//                        holder.draftNotify.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.draftNotify.setVisibility(View.GONE);
//                    }
//                }
//            }
//            holder.content.setText(content);
//            setSmilyContent(fragment.getActivity(), content, holder);
//            holder.time.setText(IMUtil.getFormatTime(conversation.getLatestTimeInMillisecond(), imKit.getIMCore().getServerTime()));
//            return convertView;
//        }
        return super.getCustomItemView(fragment, conversation, convertView, viewType, headLoadHelper, parent);
    }

    private Map<String, CharSequence> mSmilyContentCache = new HashMap<String, CharSequence>();  //表情的本地缓存，加速读取速度用
    IMSmilyCache smilyManager;
    int defaultSmilySize = 0;
    private int contentWidth;


    private void setSmilyContent(Context context, String content, ViewHolder2 holder){
        initSmilyManager(context);
        if (content == null || holder.content.getPaint() == null) {
            CharSequence charSequence = mSmilyContentCache.get(content);
            if (charSequence != null) {
                holder.content.setText(charSequence);
            } else {
                CharSequence smilySpanStr = smilyManager.getSmilySpan(context,
                        content, defaultSmilySize, false);
                mSmilyContentCache.put(content, smilySpanStr);
                holder.content.setText(smilySpanStr);
            }
        } else {
            CharSequence charSequence = mSmilyContentCache.get(content);
            if (charSequence != null) {
                holder.content.setText(charSequence);
            } else {
                CharSequence text = TextUtils.ellipsize(content,
                        holder.content.getPaint(), contentWidth,
                        holder.content.getEllipsize());
                CharSequence smilySpanStr = smilyManager.getSmilySpan(context,
                        String.valueOf(text), defaultSmilySize, false);
                mSmilyContentCache.put(content, smilySpanStr);
                holder.content.setText(smilySpanStr);
            }
        }
    }

    private void initSmilyManager(Context context){
        if (smilyManager == null){
            smilyManager = IMSmilyCache.getInstance();
            defaultSmilySize = (int) context.getResources().getDimension(R.dimen.aliwx_smily_column_width);
            int width = context.getResources().getDisplayMetrics().widthPixels;
            contentWidth = width
                    - context.getResources().getDimensionPixelSize(R.dimen.aliwx_column_up_unit_margin)*2
                    - context.getResources().getDimensionPixelSize(R.dimen.aliwx_common_head_size)
                    - context.getResources().getDimensionPixelSize(R.dimen.aliwx_message_content_margin_right);
        }
    }

    public class ViewHolder1{
        ImageView head;
        TextView name;
        TextView unread;
    }

    public class ViewHolder2{
        ImageView head;
        TextView unread;
        TextView name;
        TextView content;
        TextView atMsgNotify;
        TextView draftNotify;
        TextView time;
    }
}
