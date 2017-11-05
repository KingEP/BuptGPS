package com.kingep.buptsse.buptgps.core;

import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kingep.buptsse.buptgps.BaseActivity;
import com.kingep.buptsse.buptgps.MainWindowActivity;
import com.kingep.buptsse.buptgps.R;
import com.kingep.buptsse.buptgps.databinding.ActivityRegisterBinding;
import com.kingep.buptsse.buptgps.http.BaseOkHttpCallBack;
import com.kingep.buptsse.buptgps.http.RegisterManagerClient;


import java.io.IOException;

import okhttp3.Call;

public class RegisterActivity extends BaseActivity {
    private String sex = "男";
    private ActivityRegisterBinding mBinding;

    @Override
    protected void getLayoutResource() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mBinding.gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.boy:
                        sex = "男";
                        break;
                    case R.id.girl:
                        sex = "女";
                        break;
                }
            }
        });
    }

    @Override
    protected void initView() {
        mBinding.registerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mBinding.usernameText.getText().toString();
                String password = mBinding.passwordText.getText().toString();
                String repassword=mBinding.repasswordText.getText().toString();
                //sex在选中的时候被赋值。默认赋值为男

                if(TextUtils.isEmpty(username)){
                    mBinding.usernameText.setError("用户名不能为空！");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mBinding.passwordText.setError("密码不能为空！");
                    return;
                }
                if(TextUtils.isEmpty(repassword)){
                    mBinding.repasswordText.setError("请确认密码！");
                    return;
                }
                Toast.makeText(RegisterActivity.this,sex,Toast.LENGTH_SHORT).show();
                RegisterManagerClient.register_post(username, password,repassword,sex,new BaseOkHttpCallBack() {
                    @Override
                    public void OnSuccess(String content, int result, String resultDescription) {
                        Toast.makeText(RegisterActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                        JumpToActivity(LoginActivity.class);
                        RegisterActivity.this.finish();
                    }

                    @Override
                    public void OnError(String desc, int result) {
                        Toast.makeText(RegisterActivity.this,"注册失败，"+desc,Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void OnFailure(Call call, IOException e) {

                    }
                });
//
//                JumpToActivity(LoginActivity.class);
//                RegisterActivity.this.finish();

            }
        });
    }
}
