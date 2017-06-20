package com.toulan.bean;

import com.fast.library.utils.AndroidInfoUtils;
import com.fast.library.utils.StringUtils;

/**
 * 说明：AppVersionInfoBean
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/19 22:13
 * <p/>
 * 版本：verson 1.0
 */
public class AppVersionInfoBean {

    /**
     * version : 1.0.2
     * download : http://91toulan-bucket.oss-cn-hangzhou.aliyuncs.com/app/2016101820152/last.apk
     * remark : 升级描述：1.更新列表显示问题。2：修复部分机型闪退……
     */

    private String version;
    private String download;
    private String remark;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isUpdate(){
        return !StringUtils.isEmpty(getDownload()) && !StringUtils.isEquals(getVersion(), AndroidInfoUtils.versionName());
    }
}
