package com.kingep.buptsse.buptgps;

import android.app.Application;
import android.text.TextUtils;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.kingep.buptsse.buptgps.utils.ApplicationUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by enpeng.wang on 2017/10/28.
 */

public class MyApplication extends Application {
  private static String TAG = "MyApplication";

  @Override
  public void onCreate() {
    super.onCreate();
    SDKInitializer.initialize(this);
    //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
    //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
    SDKInitializer.setCoordType(CoordType.BD09LL);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
        .connectTimeout(10000L, TimeUnit.MILLISECONDS)
        .readTimeout(10000L, TimeUnit.MILLISECONDS)
        //其他配置
        .build();

    OkHttpUtils.initClient(okHttpClient);
    ApplicationUtil.init(this);
  }
}
