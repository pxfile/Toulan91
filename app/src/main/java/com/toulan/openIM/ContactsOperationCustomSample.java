package com.toulan.openIM;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMContactsOperation;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.YWContactManager;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.ui.contact.util.RandomNameUtil;
import com.alibaba.mobileim.utility.ToastHelper;
import com.toulan.utils.OpenImUtils;

/**
 * 说明：ContactsOperationCustomSample
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/30 16:15
 * <p/>
 * 版本：verson 1.0
 */
public class ContactsOperationCustomSample extends IMContactsOperation {

    private String TAG=ContactsOperationCustomSample.class.getSimpleName();

    public ContactsOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 是否同步联系人在线状态
     *
     * @param fragment 联系人页面fragment
     * @param context  联系人页面context
     * @return
     */
    @Override
    public boolean enableSyncContactOnlineStatus(Fragment fragment, Context context) {
        return true;
    }

    /**
     * 定制点击事件
     *
     * @param fragment
     * @param contact
     * @return true: 使用用户自定义点击事件；false：使用默认点击事件
     */
    @Override
    public boolean onListItemClick(Fragment fragment, IYWContact contact) {
        if (contact.getAppKey().equals(YWConstants.YWSDKAppKey)) {
            //TB或千牛客的服账号
            EServiceContact eServiceContact = new EServiceContact(contact.getUserId(), 0);//
            eServiceContact.setNeedByPass(false);
            Intent intent = OpenImUtils.getImKit().getChattingActivityIntent(eServiceContact);
            if(intent!=null)
                fragment.getActivity().startActivity(intent);
        } else {
            Intent intent =OpenImUtils.getImKit().getChattingActivityIntent(contact.getUserId(), contact.getAppKey());
            if(intent!=null)
                fragment.getActivity().startActivity(intent);
        }
        return true;
    }

    /**
     * 定制长按事件
     *
     * @param fragment
     * @param contact
     * @return true: 使用用户自定义长按事件；false：使用默认长按事件
     */
    @Override
    public boolean onListItemLongClick(final Fragment fragment, final IYWContact contact) {
        final FragmentActivity mContext = fragment.getActivity();
        final IYWContactService contactService = OpenImUtils.getImKit().getContactService();
        final String[] items = new String[4];
        boolean isBlocked = contactService.isBlackContact(contact.getUserId(), contact.getAppKey());
        items[0] = "个人资料";
        if (isBlocked) {
            items[1] = "移除黑名单";
        } else {
            items[1] = "加入黑名单";
        }
        items[2] = "删除好友";
        items[3] = "修改备注";
        if(!YWContactManager.isBlackListEnable()) {
            YWContactManager.enableBlackList();
        }
        //此处为示例代码
        new YWAlertDialog.Builder(mContext)
                .setTitle(contact.getShowName())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("个人资料")){
                            if (fragment != null && fragment.getActivity() != null){
                                Intent intent = new Intent(fragment.getActivity(),YwUserInfoActivity.class);
                                intent.putExtra(YwUserInfoActivity.USERID,contact.getUserId());
                                fragment.getActivity().startActivity(intent);
                            }
                        } else if (items[which].equals("加入黑名单")) {
                            contactService.addBlackContact(contact.getUserId(), contact.getAppKey(), new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) result[0];
                                    YWLog.i(TAG, "加入黑名单成功, id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "加入黑名单成功, id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "加入黑名单失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "加入黑名单失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        } else if (items[which].equals("移除黑名单")) {
                            contactService.removeBlackContact(contact.getUserId(), contact.getAppKey(), new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) result[0];
                                    YWLog.i(TAG, "移除黑名单成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "移除黑名单成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "移除黑名单失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "移除黑名单失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }else if (items[which].equals("删除好友")) {
                            contactService.delContact(contact.getUserId(), contact.getAppKey(), new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) result[0];
                                    YWLog.i(TAG, "删除好友成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "删除好友成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "删除好友失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "删除好友失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }else if (items[which].equals("修改备注")) {
                            final String lRandomName = RandomNameUtil.getRandomName();
                            contactService.chgContactRemark(contact.getUserId(), contact.getAppKey(),lRandomName, new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) contact;
                                    YWLog.i(TAG, "修改备注成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "修改备注成功,  id = " + iywContact.getUserId() + " , appkey = " + iywContact.getAppKey()+" , 备注名 ＝ "+lRandomName);
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "修改备注失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "修改备注失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
        return true;
    }
}

