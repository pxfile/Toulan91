package com.toulan.model;

import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RestRequest;

import org.json.JSONObject;

/**
 * 说明：请求
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 21:30
 * <p/>
 * 版本：verson 1.0
 */
public class TJsonRequest extends RestRequest<JSONObject>{

    public TJsonRequest(String url) {
        this(url,RequestMethod.POST);
    }

    public TJsonRequest(String url, RequestMethod requestMethod){
        super(url,requestMethod);
        setAccept("application/json");
    }

    @Override
    public JSONObject parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        return null;
    }
}
