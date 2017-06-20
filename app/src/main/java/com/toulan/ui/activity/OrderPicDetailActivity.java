package com.toulan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast.library.banner.BannerView;
import com.fast.library.banner.holder.BannerHolderCreator;
import com.fast.library.banner.holder.Holder;
import com.fast.library.glide.GlideLoader;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.toulan.bean.OrderBean;
import com.toulan.model.OnStringListener;
import com.toulan.model.TDataManager;
import com.toulan.ui.CommonActivity;
import com.toulan.utils.TConstant;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;

/**
 * 说明：OrderPicDetailActivity
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/27 15:39
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_order_pic_detail)
public class OrderPicDetailActivity extends CommonActivity {

    public final static String DATA = "data";

    @Bind(R.id.bannerView)
    BannerView bannerView;

    TextView mTitleTv;
    OrderBean mOrder;
    private ArrayList<String> arrayList;
    private BannerHolderCreator<BannerHolder> bannerHolderCreator;
    private final static int REQUEST_CODE_PIC = 101;

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        mOrder = GsonUtils.toBean(intent.getStringExtra(DATA),OrderBean.class);
    }

    @Override
    public void onInitStart() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (mOrder != null && mOrder.getOrderPic() != null && !mOrder.getOrderPic().isEmpty()){
            init(mOrder.getOrderPic());
        }
    }

    private void init(List<String> list){
        if (bannerHolderCreator == null){
            bannerHolderCreator = new BannerHolderCreator<BannerHolder>() {
                @Override
                public BannerHolder createHolder() {
                    return new BannerHolder();
                }
            };
        }
        bannerView.setCanLoop(list.size() != 1);
        bannerView.setPages(bannerHolderCreator,list);
        bannerView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPicTitle(mOrder.getOrderPic().size(),position+1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private class BannerHolder implements Holder<String>{

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            View view = UIUtils.inflate(R.layout.item_pic_detail);
            imageView = (ImageView) view.findViewById(R.id.iv_main_master_room_banner);
            return view;
        }

        @Override
        public void convert(final Context context, int position, final String item) {
            GlideLoader.into(item,imageView);
        }
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(cn.bingoogolapple.photopicker.R.menu.bga_pp_menu_photo_preview, menu);
        MenuItem menuItem = menu.findItem(cn.bingoogolapple.photopicker.R.id.item_photo_preview_title);
        View actionView = menuItem.getActionView();

        mTitleTv = (TextView) actionView.findViewById(cn.bingoogolapple.photopicker.R.id.tv_photo_preview_title);
        TextView tvJixu = (TextView) actionView.findViewById(cn.bingoogolapple.photopicker.R.id.tv_photo_picker_jixu);
        tvJixu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File takePhotoDir = new File(Environment.getExternalStorageDirectory(), TConstant.APP);
                arrayList = Tsp.getOrderPic(mOrder.getOrderDetail().getOid());
                startActivityForResult(BGAPhotoPickerActivity.newIntent(OrderPicDetailActivity.this,mOrder.getOrderDetail().getOid(),takePhotoDir,9,arrayList,true),REQUEST_CODE_PIC);
            }
        });
        ImageView mDownloadIv = (ImageView) actionView.findViewById(cn.bingoogolapple.photopicker.R.id.iv_photo_preview_download);

        mDownloadIv.setVisibility(View.INVISIBLE);

        if (mOrder != null && mOrder.getOrderPic() != null && !mOrder.getOrderPic().isEmpty()){
            setPicTitle(mOrder.getOrderPic().size(),1);
        }
        return true;
    }

    private void setPicTitle(final int count, final int current){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTitleTv != null){
                    mTitleTv.setText("云质检（"+current+"/"+count+"）");
                }
            }
        });
    }

    @Override
    protected void onCustomToolbar(View view, Toolbar toolbar) {
        super.onCustomToolbar(view, toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PIC) {
            arrayList = BGAPhotoPickerActivity.getSelectedImages(data);
            int oid = BGAPhotoPickerActivity.getOrderId(data);
            if (!arrayList.isEmpty() && oid != 0){
                submitPic(oid,arrayList);
            }
        }
    }

    private void uploadPic(String token,final int oid, final ArrayList<String> list){
        TDataManager.getInstance().uploadFile(token, list, new OnStringListener() {
            @Override
            public void onStart(int what) {
                showLoading(false,false);
            }
            @Override
            public void onFinish(int what) {
                dismissLoading();
                if (!OrderPicDetailActivity.this.isFinishing()){
                    shortToast("上传成功");
                    OrderPicDetailActivity.this.finish();
                }
            }

            @Override
            public void onSuccess(int what, String node) {
                try {
                    JSONObject object = new JSONObject(node);
                    int failed = object.getJSONArray("failed").length();
                    if (failed != list.size()){
                        shortToast("上传成功");
                    }
                    if (!OrderPicDetailActivity.this.isFinishing()){
                        OrderPicDetailActivity.this.finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int what, int code, String msg) {
                shortToast(msg);
            }
        });
    }

    private void submitPic(final int oid, final ArrayList<String> pics){
        TDataManager.getInstance().uploadToken(oid, new OnStringListener() {
            @Override
            public void onStart(int what) {
                showLoading();
            }
            @Override
            public void onFinish(int what) {
                dismissLoading();
            }
            @Override
            public void onSuccess(int what, String node) {
                String token = GsonUtils.optString(node,"uploadToken");
                if (!StringUtils.isEmpty(token)){
                    uploadPic(token,oid,pics);
                }else {
                    shortToast("上传错误！");
                }
            }
            @Override
            public void onFail(int what, int code, String msg) {
                shortToast("上传错误！");
            }
        });
    }
}
