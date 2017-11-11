package com.kingep.buptsse.buptgps.core;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Handler;
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
import com.kingep.buptsse.buptgps.utils.ApplicationUtil;


import java.io.IOException;

import okhttp3.Call;

public class RegisterActivity extends BaseActivity {
  private String sex = "男";
  private ActivityRegisterBinding mBinding;
  private Handler mHandler = new Handler();

  @Override
  protected void getLayoutResource() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
    mBinding.gendergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
          case R.id.boy:
            sex = "男";
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                mBinding.imageview2.setImageResource(R.drawable.male_avatar);
              }
            });
            break;
          case R.id.girl:
            sex = "女";
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                mBinding.imageview2.setImageResource(R.drawable.female_avatar);
              }
            });
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
        String repassword = mBinding.repasswordText.getText().toString();
        //sex在选中的时候被赋值。默认赋值为男

        if (TextUtils.isEmpty(username)) {
          mBinding.usernameText.setError("用户名不能为空！");
          return;
        }
        if (TextUtils.isEmpty(password)) {
          mBinding.passwordText.setError("密码不能为空！");
          return;
        }
        if (TextUtils.isEmpty(repassword)) {
          mBinding.repasswordText.setError("请确认密码！");
          return;
        }
        if (!mBinding.repasswordText.getText().equals(mBinding.passwordText.getText())) {
          mBinding.repasswordText.setError("两次密码不一致！");
        }

        SharedPreferences sharedPreferences = ApplicationUtil.getApplication().getSharedPreferences("BuptGPS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", mBinding.usernameText.getText().toString());
        editor.putString("password", mBinding.passwordText.getText().toString());
        editor.putString("gender", sex.equals("男") ? "0" : "1");
        if (editor.commit()) {
          Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT);
          finish();
        } else {
          Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT);
        }
//                Toast.makeText(RegisterActivity.this,sex,Toast.LENGTH_SHORT).show();
//                RegisterManagerClient.register_post(username, password,repassword,sex,new BaseOkHttpCallBack() {
//                    @Override
//                    public void OnSuccess(String content, int result, String resultDescription) {
//                        Toast.makeText(RegisterActivity.this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
//                        JumpToActivity(LoginActivity.class);
//                        RegisterActivity.this.finish();
//                    }
//
//                    @Override
//                    public void OnError(String desc, int result) {
//                        Toast.makeText(RegisterActivity.this,"注册失败，"+desc,Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void OnFailure(Call call, IOException e) {
//
//                    }
//                });
//
//                JumpToActivity(LoginActivity.class);
//                RegisterActivity.this.finish();

      }
    });
  }
}
