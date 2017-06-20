package com.toulan.openIM;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.IYWDBContact;
import com.alibaba.mobileim.utility.ToastHelper;
import com.fast.library.ui.ContentView;
import com.toulan.ui.CommonActivity;
import com.toulan.utils.OpenImUtils;
import com.toulan91.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 说明：YwUserInfoActivity
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/10/8 15:52
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.demo_container_activity)
public class YwUserInfoActivity extends CommonActivity implements IParent{

    public final static String USERID = "userid";
    private Handler mHandler=new Handler();
    String userId,APPKEY;
    private List<IYWDBContact> contactsFromCache;

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        userId = intent.getStringExtra(USERID);
        APPKEY= OpenImUtils.getImKit().getIMCore().getAppKey();
    }

    @Override
    public void onInitStart() {
        ArrayList<String> userIds = new ArrayList<String>();
        userIds.add(userId);
        IYWContactService contactService = OpenImUtils.getImKit().getContactService();
        contactService.fetchUserProfile(userIds, APPKEY, new IWxCallback() {

            @Override
            public void onSuccess(final Object... result) {
                if (result != null) {
                    List<YWProfileInfo> profileInfos = (List<YWProfileInfo>) (result[0]);
                    if (profileInfos == null || profileInfos.isEmpty()) {
                        return;
                    }
                    YWProfileInfo mYWProfileInfo = profileInfos.get(0);
                    checkIfHasContact(mYWProfileInfo);
                    showSearchResult(mYWProfileInfo);
                }
            }

            @Override
            public void onError(int code, String info) {
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    private void checkIfHasContact(YWProfileInfo mYWProfileInfo){
        //修改hasContactAlready和contactsFromCache的Fragment生命周期缓存
        contactsFromCache =OpenImUtils.getImKit().getContactService().getContactsFromCache();
        for(IYWDBContact contact:contactsFromCache){
            if(contact.getUserId().equals(mYWProfileInfo.userId)){
                setHasContactAlready(true);
                return;
            }
        }
        setHasContactAlready(false);
    }

    public void showSearchResult(final YWProfileInfo lmYWProfileInfo){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (lmYWProfileInfo == null || TextUtils.isEmpty(lmYWProfileInfo.userId)) {
                    ToastHelper.showToastMsg(YwUserInfoActivity.this, "服务开小差");
                    return;
                }
                setYWProfileInfo(lmYWProfileInfo);
                addFragment(new ContactProfileFragment(), true);
            }
        });

    }

    //跳转相关
    @Override
    public void addFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.wx_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
		getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1){
            finish();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish(boolean POP_BACK_STACK_INCLUSIVE) {
        if(POP_BACK_STACK_INCLUSIVE){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }else{
            getSupportFragmentManager().popBackStack();
        }
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 1){
            finish();
        }
    }

    //传递数据用，由father来持有
    private YWProfileInfo ywProfileInfo;
    //传递数据用，由father来持有
    private boolean hasContactAlready;

    public YWProfileInfo getYWProfileInfo() {
        return ywProfileInfo;
    }
    @Override
    public void setYWProfileInfo(YWProfileInfo ywProfileInfo) {
        this.ywProfileInfo = ywProfileInfo;
    }
    @Override
    public boolean isHasContactAlready() {
        return hasContactAlready;
    }
    @Override
    public void setHasContactAlready(boolean hasContactAlready) {
        this.hasContactAlready = hasContactAlready;
    }
}
