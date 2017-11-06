package com.kingep.buptsse.buptgps;

import android.app.Application;
import android.text.TextUtils;

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
