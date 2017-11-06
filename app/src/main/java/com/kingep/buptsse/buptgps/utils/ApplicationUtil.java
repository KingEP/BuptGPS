package com.kingep.buptsse.buptgps.utils;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

/**
 * Created by enpeng.wang on 2017/10/28.
 */


public class ApplicationUtil {

  private static Application application;
  public static boolean smsReceiverHasRegister;

  public static void init(Application application) {
    ApplicationUtil.application = application;
    smsReceiverHasRegister = false;
  }

  public static Application getApplication() {
    if (application != null) {
      return application;
    } else {
      Class<?> aClass = null;
      try {
        aClass = Class.forName("android.app.ActivityThread");
        Method currentApplication = aClass.getDeclaredMethod("currentApplication");
        Application application = (Application) currentApplication.invoke(null);
        ApplicationUtil.application = application;
        return application;
      } catch (Exception e) {
        return null;
      }
    }
  }

  public static Context getContext() {
    try {
      return getApplication().getApplicationContext();
    } catch (Throwable e) {
      throw new IllegalArgumentException("null application");
    }
  }

}