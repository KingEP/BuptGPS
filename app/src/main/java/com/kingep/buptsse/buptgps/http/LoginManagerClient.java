package com.kingep.buptsse.buptgps.http;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.kingep.buptsse.buptgps.http.BaseOkHttpClient.callOkHttpRequest;

/**
 * Created by Yu on 2017/5/24.
 */

public class LoginManagerClient {
    private static BaseOkHttpClient client = BaseOkHttpClient.getInstance();
//    /**
//     * okhttp登陆 get请求方式
//     * @return
//     */
//    public static Call login_get(String username,String password,BaseOkHttpCallBack baseOkHttpCallBack){
//        Request request = new Request.Builder().url(client.getAbsoluteUrl("loginServlet?username="+username+"&password="+password)).build();
//       // Request request = new Request.Builder().url("http://www.baidu.com").build();
//        return callOkHttpRequest(request,baseOkHttpCallBack);
//    }
    /**
     * okhttp登陆 Post请求方式
     * @return
     */
    public static Call login_post(String username,String password,BaseOkHttpCallBack baseOkHttpCallBack){
        RequestBody body = new FormBody.Builder().add("username",username).add("password",password).build();
        Request request = new Request.Builder().post(body).url(client.getAbsoluteUrl("loginServlet")).build();
        return callOkHttpRequest(request,baseOkHttpCallBack);
    }
    protected static Call callOkHttpRequest(Request request, BaseOkHttpCallBack baseOkHttpCallBack){
        return client.callOkHttpRequest(request,baseOkHttpCallBack);
    }
}
