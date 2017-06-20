package com.toulan.openIM;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMContactsOperation;
import com.alibaba.mobileim.aop.custom.IMContactsUI;
import com.toulan91.R;

/**
 * 说明：
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/28 10:44
 * <p/>
 * 版本：verson 1.0
 */
public class ContactsUiCustom extends IMContactsUI {


    public ContactsUiCustom(Pointcut pointcut) {
        super(pointcut);
    }
    /**
     * 返回联系人自定义标题
     *
     * @param fragment
     * @param context
     * @param inflater
     * @return
     */
    @Override
    public View getCustomTitle(final Fragment fragment, final Context context, LayoutInflater inflater) {
        RelativeLayout customView = (RelativeLayout) inflater
                .inflate(R.layout.custom_contacts_title_bar, new RelativeLayout(context), false);
        customView.setBackgroundColor(Color.parseColor("#03ccf5"));
        TextView title = (TextView) customView.findViewById(R.id.title_txt);
        title.setText("联系人");
        title.setTextSize(15);
        title.setTextColor(Color.WHITE);
        TextView backButton = (TextView) customView.findViewById(R.id.left_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fragment.getActivity().finish();
            }
        });
        backButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.common_back_btn_white, 0, 0, 0);
        backButton.setVisibility(View.VISIBLE);
        ImageView rightButton = (ImageView) customView.findViewById(R.id.title_button);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent().setClass(context, FindContactActivity.class);
                context.startActivity(intent);
            }
        });
        return customView;
    }

}
