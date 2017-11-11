package com.kingep.buptsse.buptgps.core;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kingep.buptsse.buptgps.BaseActivity;
import com.kingep.buptsse.buptgps.MainWindowActivity;
import com.kingep.buptsse.buptgps.R;
import com.kingep.buptsse.buptgps.databinding.ActivityLoginBinding;
import com.kingep.buptsse.buptgps.http.BaseOkHttpCallBack;
import com.kingep.buptsse.buptgps.http.LoginManagerClient;
import com.kingep.buptsse.buptgps.utils.ApplicationUtil;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Dell on 2017/10/30.
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;

    @Override
    protected void getLayoutResource() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    }

    @Override
    protected void initView() {
        mBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mBinding.usernameText.getText().toString();
                String password = mBinding.passwordText.getText().toString();

                if(TextUtils.isEmpty(username)){
                    mBinding.usernameText.setError("用户名不能为空！");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mBinding.passwordText.setError("密码不能为空！");
                    return;
                }
//                LoginManagerClient.login_post(username, password, new BaseOkHttpCallBack() {
//                    @Override
//                    public void OnSuccess(String content, int result, String resultDescription) {
//                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//                        JumpToActivity(MainWindowActivity.class);
//                        LoginActivity.this.finish();
//                    }
//
//                    @Override
//                    public void OnError(String desc, int result) {
//                        Toast.makeText(LoginActivity.this,"登录失败，"+desc,Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void OnFailure(Call call, IOException e) {
//
//                    }
//                });

                SharedPreferences sharedPreferences = ApplicationUtil.getApplication().getSharedPreferences("BuptGPS", MODE_PRIVATE);
                String sharedUserName = sharedPreferences.getString("userName","");
                String sharedPassWord = sharedPreferences.getString("password","");
                if(sharedUserName.equals(username) && sharedPassWord.equals(password)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT);
                    JumpToActivity(MainWindowActivity.class);
                    LoginActivity.this.finish();
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT);
                }

            }
        });
        mBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpToActivity(RegisterActivity.class);

            }
        });
    }
}
