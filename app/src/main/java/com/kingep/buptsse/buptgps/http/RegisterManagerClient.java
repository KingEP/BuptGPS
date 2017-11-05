package com.kingep.buptsse.buptgps.http;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.kingep.buptsse.buptgps.http.BaseOkHttpClient.callOkHttpRequest;

/**
 * Created by Dell on 2017/10/31.
 */

public class RegisterManagerClient {

    private static BaseOkHttpClient client = BaseOkHttpClient.getInstance();

//    /**
//     * okhttp登陆 get请求方式
//     *
//     * @return
//     */
//    public static Call register_get(String username, String password, String repassword,String sex, BaseOkHttpCallBack baseOkHttpCallBack) {
//        Request request = new Request.Builder().url(client.getAbsoluteUrl("registerServlet?username=" + username + "&password=" + password + "&repassword" + repassword+"&sex"+sex)).build();
//        // Request request = new Request.Builder().url("http://www.baidu.com").build();
//        return callOkHttpRequest(request, baseOkHttpCallBack);
//    }

    /**
     * okhttp登陆 Post请求方式
     *
     * @return
     */
    public static Call register_post(String username, String password, String repassword,String sex,BaseOkHttpCallBack baseOkHttpCallBack) {
        RequestBody body = new FormBody.Builder().add("username", username).add("password", password).add("repassword", repassword).add("sex",sex).build();
        Request request = new Request.Builder().post(body).url(client.getAbsoluteUrl("registerServlet")).build();
        return callOkHttpRequest(request, baseOkHttpCallBack);
    }

    protected static Call callOkHttpRequest(Request request, BaseOkHttpCallBack baseOkHttpCallBack) {
        return client.callOkHttpRequest(request, baseOkHttpCallBack);
    }
}
