package com.kingep.buptsse.buptgps.http;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Yu on 2017/6/19.
 */

public abstract class BaseOkHttpCallBack implements okhttp3.Callback {
    private final static String TAG = "BaseOkHttpCallBack";
    private static final String KEY_CONTENT = "content";                //根据实际情况修改
    private static final String KEY_RESULT = "result";                    //根据实际情况修改
    private static final String KEY_RESULT_DESCRIPTION = "resultDesp";    //根据实际情况修改
    public static final int STATUS_SUCCESS = 1;                        //返回报文中，请求成功 对应的编码
    public static final int STATUS_ERROR = -1;                            //返回报文中，请求失败 对应的编码
    private JSONObject jsonObject;

    //解析结果回调方法
    public abstract void OnSuccess(String content, int result, String resultDescription);

    public abstract void OnError(String desc, int result);

    public abstract void OnFailure(Call call, IOException e);

    @Override
    public void onFailure(Call call, IOException e) {
        OnFailure(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String responseStr = response.body().string();
        if (responseStr == null) {
            throw new NullPointerException("response body is null！");
        }
        try {
            jsonObject = new JSONObject(responseStr);
        } catch (Exception e) {
            throw new RuntimeException("response body is not json！");
        }
        int result = response.code();
        Log.i(TAG, "onResponse: result:" + result + " , responseBody:" + jsonObject.toString());
        if (200 == result) {
            if (jsonObject.optInt(KEY_RESULT) == STATUS_ERROR) {
                OnError(jsonObject.optString(KEY_RESULT_DESCRIPTION), result);
            } else {
                OnSuccess(jsonObject.optString(KEY_CONTENT), result, jsonObject.optString(KEY_RESULT_DESCRIPTION));
            }
        } else {
            OnError("网络链接失败", result);
        }
    }
}