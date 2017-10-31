package com.kingep.buptsse.buptgps.http;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Yu on 2017/5/25.
 */

public class BaseOkHttpClient {
    private static final String BASE_URL = "http://10.107.8.181:8080/MHXY/";//mConfig.BaseURL;
    private static final BaseOkHttpClient baseOkHttpClient = new BaseOkHttpClient();
    private static OkHttpClient client;

    /**
     * 单例模式获得AsyncHttpClient对象
     * @return
     */
    public static BaseOkHttpClient getInstance(){
        return baseOkHttpClient;
    }
    private BaseOkHttpClient() {
        initClient();
    }

    private static void initClient() {
        client = new OkHttpClient();
    }
    protected static Call callOkHttpRequest(Request request,BaseOkHttpCallBack baseOkHttpCallBack){
        Response response=null;
//        try {
            Call newCall = client.newCall(request);
            newCall.enqueue(baseOkHttpCallBack);
            //response = client.newCall(request).execute();
       /* } catch (IOException e1) {
            e1.printStackTrace();
        }
        */
        return newCall;
    }
    //传入时只需传绝对路径地址，将根目录连接
    protected static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
