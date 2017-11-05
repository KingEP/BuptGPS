基于： compile 'com.squareup.okhttp3:okhttp:3.3.0'
要求服务端返回的结果为json格式，并且，组织数据的格式为：
{
    "result":1/-1,                      --"KEY_RESULT":  STATUS_SUCCESS/STATUS_ERROR
    "resultDesp":"resultDespContent",   --"KEY_RESULT_DESCRIPTION":"resultDespContent",
    "content":"contentJson"             --"KEY_CONTENT":"contentJson"
}

BaseOkHttpCallBack中的关键字
   String KEY_CONTENT = "content";                  //根据实际情况修改
   String KEY_RESULT = "result";                    //根据实际情况修改
   String KEY_RESULT_DESCRIPTION = "resultDesp";    //根据实际情况修改
   int STATUS_SUCCESS = 1;                          //返回报文中，请求成功 对应的编码
   int STATUS_ERROR = -1;                           //返回报文中，请求失败 对应的编码

调用方法返回的Call 可用来取消请求的发送

登录：username，password
注册：username，password，repassword，sex