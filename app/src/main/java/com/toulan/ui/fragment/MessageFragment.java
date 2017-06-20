package com.toulan.ui.fragment;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.util.WxLog;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.gingko.presenter.tribe.IYWTribeChangeListener;
import com.fast.library.tools.EventCenter;
import com.fast.library.ui.ContentView;
import com.toulan.bean.UserBean;
import com.toulan.openIM.UserProfileSampleHelper;
import com.toulan.ui.activity.ActivityContacts;
import com.toulan.ui.activity.ActivityTribe;
import com.toulan.utils.OpenImUtils;
import com.toulan.utils.TConstant;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 说明：MessageFragment
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 16:02
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.fragment_message)
public class MessageFragment extends CommonFragment {

    @Bind(R.id.toolbar_message)
    Toolbar mToolbar;
    @Bind(R.id.tv_message_name)
    TextView tvName;

    UserBean mUserBean;
    private IYWTribeChangeListener mTribeChangedListener;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private IYWConversationService mConversationService;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private YWIMKit mIMKit;
    private List<Map<YWTribe, YWTribeMember>> mTribeInviteMessages = new ArrayList<Map<YWTribe, YWTribeMember>>();

    @Override
    protected void onUserVisible() {
        super.onUserVisible();
        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        mConversationUnreadChangeListener.onUnreadChange();
    }

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        setHasOptionsMenu(true);
        mCommonActivity.setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        mUserBean = Tsp.getUserInfo();
        tvName.setText(mUserBean.getName());

        mIMKit = OpenImUtils.getImKit();
        if (mIMKit == null) {
            return;
        }
        mConversationService = mIMKit.getConversationService();

        android.support.v4.app.FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();
        transaction.add(R.id.fl_message_list, OpenImUtils.getConversationFragment(), OpenImUtils.getConversationFragment().getClass()
                .getName());
        transaction.commit();

        initConversationServiceAndListener();
        addTribeChangeListener();
        UserProfileSampleHelper.initProfileCallback();
    }

    private void initConversationServiceAndListener() {
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {
            //当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final YWIMKit imKit = OpenImUtils.getImKit();
                        mConversationService = imKit.getConversationService();
                        //获取当前登录用户的所有未读数
                        int unReadCount = mConversationService.getAllUnreadCount();
                        //设置桌面角标的未读数
                        mIMKit.setShortcutBadger(unReadCount);
                        EventBus.getDefault().post(new EventCenter<Integer>(TConstant.EventType.mainShowMessageNumber,unReadCount));
                    }
                });
            }
        };
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }

    private void addTribeChangeListener(){
        mTribeChangedListener = new IYWTribeChangeListener() {
            @Override
            public void onInvite(YWTribe tribe, YWTribeMember user) {
                Map<YWTribe, YWTribeMember> map = new HashMap<YWTribe, YWTribeMember>();
                map.put(tribe, user);
                mTribeInviteMessages.add(map);
                String userName = user.getShowName();
                if (TextUtils.isEmpty(userName)){
                    userName = user.getUserId();
                }
            }

            @Override
            public void onUserJoin(YWTribe tribe, YWTribeMember user) {
                //用户user加入群tribe
            }

            @Override
            public void onUserQuit(YWTribe tribe, YWTribeMember user) {
                //用户user退出群tribe
            }

            @Override
            public void onUserRemoved(YWTribe tribe, YWTribeMember user) {
                //用户user被提出群tribe
            }

            @Override
            public void onTribeDestroyed(YWTribe tribe) {
                //群组tribe被解散了
            }

            @Override
            public void onTribeInfoUpdated(YWTribe tribe) {
                //群组tribe的信息更新了（群名称、群公告、群校验模式修改了）
            }

            @Override
            public void onTribeManagerChanged(YWTribe tribe, YWTribeMember user) {
                //用户user被设置为群管理员或者被取消管理员
            }

            @Override
            public void onTribeRoleChanged(YWTribe tribe, YWTribeMember user) {
                //用户user的群角色发生改变了
            }
        };
        mIMKit.getTribeService().addTribeListener(mTribeChangedListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_message_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_message_contact:
                showActivity(ActivityContacts.class);
                break;
            case R.id.item_message_qunzu:
                showActivity(ActivityTribe.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
