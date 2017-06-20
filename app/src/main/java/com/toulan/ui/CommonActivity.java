package com.toulan.ui;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fast.library.BaseActivity;
import com.fast.library.utils.UIUtils;
import com.toulan.ui.dialog.CommandDialog;
import com.toulan.ui.dialog.LoadingDialog;
import com.toulan.utils.ToolbarHelper;
import com.toulan91.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * 说明：CommonActivity
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 9:20
 * <p/>
 * 版本：verson 1.0
 */
public abstract class CommonActivity extends BaseActivity{

    private ToolbarHelper toolbarHelper;
    private Toolbar toolbar;
    private TextView toolbarTiltle;

//    等待框
    private LoadingDialog mLoadingDialog;
//    命令框
    private CommandDialog mCommandDialog;

    @Override
    public int createLoaderID() {
        return 0;
    }

    @Override
    public void onInitCreate(Bundle bundle) {
        if (isRegisterEventBus()){
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    /********************************菜单设置*********************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (setMenu() != 0){
            getMenuInflater().inflate(setMenu(), menu);
            return true;
        }else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (setMenu() != 0){
            onMenuItemClick(item.getItemId());
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 说明：菜单点击处理
     * @param id
     */
    protected void onMenuItemClick(int id){}

    /**
     * 说明：设置菜单选项
     * @return
     */
    protected int setMenu(){
        return 0;
    }

    /********************************菜单设置*********************************************/


    /**********************************配置Toolbar****************************************/

    /**
     * 说明：是否显示Toolbar
     * @return
     */
    protected boolean showToolbar(){
        return false;
    }

    @Override
    public void setContentView(int layoutResID) {
        if (showToolbar()){
            toolbarHelper = new ToolbarHelper(this,layoutResID);
            toolbar = toolbarHelper.getToolbar();
            toolbarTiltle = (TextView) toolbar.findViewById(R.id.id_tool_bar_title);
            setContentView(toolbarHelper.getView());
            CustomToolbar(toolbarHelper.getView(),toolbar);
            onCustomToolbarBefore(toolbarHelper.getView(),toolbar);
            setSupportActionBar(toolbar);
            onCustomToolbar(toolbarHelper.getView(),toolbar);
        }else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * 说明：自定义布局
     * @param view
     * @param toolbar
     */
    protected void onCustomToolbarBefore(View view, Toolbar toolbar){}

    /**
     * 说明：自定义布局
     * @param view
     * @param toolbar
     */
    protected void onCustomToolbar(View view, Toolbar toolbar){}

    /**
     * 设置标题
     * @param tvTitle
     */
    protected void setToolbarTitle(TextView tvTitle){}

    /**
     * 说明：自定义布局
     * @param view
     * @param toolbar
     */
    private void CustomToolbar(View view,Toolbar toolbar){
        toolbar.setTitleTextColor(UIUtils.getColor(R.color.white));
        toolbar.setContentInsetsRelative(0,0);
        toolbar.setTitle("");
        setToolbarTitle(toolbarTiltle);
    }

    /**********************************配置Toolbar****************************************/

    /******************************************************************/

    /**
     * 是否需要注册EventBus
     * @return
     */
    public boolean isRegisterEventBus(){
        return false;
    }

    /******************************************************************/

    @Override
    protected void onDestroy() {
        if (isRegisterEventBus()){
            EventBus.getDefault().unregister(this);
        }
        dismissLoading();
        dismissCommandDialog();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    /**
     * 显示等待框
     */
    public void showLoading(){
        showLoading(true,true);
    }


    /**
     * 显示等待框
     * @param isCancel 返回是否消失
     * @param isCancelOnSide 点击空白是否消失
     */
    public void showLoading(boolean isCancel,boolean isCancelOnSide){
        if (mLoadingDialog == null){
            mLoadingDialog = new LoadingDialog(this).setCancel(isCancel).setCancelOnSide(isCancelOnSide);
        }
        mLoadingDialog.setCancel(isCancel).setCancelOnSide(isCancelOnSide);
        dismissLoading();
        mLoadingDialog.show();
    }

    /**
     * 关闭等待框
     */
    public void dismissLoading(){
        if (mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 显示命令框
     * @param descRes 描述
     * @param textRes job_confirm_normal
     */
    public void showCommandDialog(@StringRes int descRes, @StringRes int textRes, CommandDialog.OnComandListener listener){
        showCommandDialog(UIUtils.getString(descRes),UIUtils.getString(textRes),listener);
    }

    /**
     * 显示命令框
     * @param desc 描述
     * @param text job_confirm_normal
     */
    public void showCommandDialog(String desc, String text, CommandDialog.OnComandListener listener){
        if (mCommandDialog == null){
            mCommandDialog = new CommandDialog(this).setDesc(desc).setConfirmText(text).setOnCommandListener(listener);
            mCommandDialog.setCancelable(false);
            mCommandDialog.setCanceledOnTouchOutside(false);
        }else {
            mCommandDialog.setConfirmText(text).setDesc(desc).setOnCommandListener(listener);
        }
        dismissCommandDialog();
        mCommandDialog.show();
    }

    /**
     * 关闭等待框
     */
    public void dismissCommandDialog(){
        if (mCommandDialog != null && mCommandDialog.isShowing()){
            mCommandDialog.dismiss();
        }
    }
}
