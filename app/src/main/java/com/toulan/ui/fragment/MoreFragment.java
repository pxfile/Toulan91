package com.toulan.ui.fragment;

import android.webkit.WebView;
import android.widget.ProgressBar;

import com.fast.library.tools.ViewTools;
import com.fast.library.ui.ContentView;
import com.fast.library.ui.WebViewLoader;
import com.fast.library.utils.StringUtils;
import com.fast.library.view.CircleProgressView;
import com.toulan.model.MoreUrlBean;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.utils.AesUtil;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import org.json.JSONObject;

import butterknife.Bind;

/**
 * 说明：MoreFragment
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 16:02
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.fragment_more)
public class MoreFragment extends CommonFragment {

    @Bind(R.id.more_web_view)
    WebView moreWebView;
    @Bind(R.id.pb_refresh)
    ProgressBar pbRefresh;
    @Bind(R.id.loading_cpv)
    CircleProgressView loadingCPV;

    private String url;
    private String time;

    private WebViewLoader.Adapter mAdapter = new WebViewLoader.Adapter() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 80){
                pbRefresh.setProgress(100);
                ViewTools.VISIBLE(pbRefresh);
            }else {
                ViewTools.GONE(pbRefresh);
            }
        }
    };

    private WebViewLoader webViewLoader;

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        webViewLoader = new WebViewLoader(moreWebView,mAdapter);
        webViewLoader.init();
    }

    @Override
    protected void onUserVisible() {
        super.onUserVisible();
        getMoreUrl();
    }

    private void showUrl(){
        if (moreWebView != null && !StringUtils.isEmpty(url)){
            moreWebView.loadUrl(url);
        }
    }

    private void getMoreUrl(){
        if (StringUtils.isEmpty(url)){
            TDataManager.getInstance().moreUrl(new OnModelListener<MoreUrlBean>(MoreUrlBean.class) {
                @Override
                public void onStart(int what) {
                    ViewTools.VISIBLE(loadingCPV);
                }

                @Override
                public void onFinish(int what) {
                    ViewTools.GONE(loadingCPV);
                }

                @Override
                public void onFail(int what, int code, String msg) {
                    shortToast(msg);
                }

                @Override
                public void onSuccess(int what, MoreUrlBean o) {
                    url = o.url + "&token=" + encodeToken() +"&time="+time;
                    showUrl();
                }
            });
        }else {
            showUrl();
        }
    }

    private String encodeToken(){
        String token = "";
        try {
            JSONObject object1 = new JSONObject();
            time = String.valueOf(System.currentTimeMillis());
            object1.put("time",time);
            object1.put("userid",Tsp.getUserInfo().getUserId());
            token = AesUtil.aesEncrypt(object1.toString(),Tsp.getUserSecret().substring(0,16));
            token.replace(" ","");
            token = StringUtils.utfEncode(token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webViewLoader.onDestroy();
    }
}
