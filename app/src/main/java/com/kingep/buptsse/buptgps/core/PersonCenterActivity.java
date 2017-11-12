package com.kingep.buptsse.buptgps.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kingep.buptsse.buptgps.R;
import com.kingep.buptsse.buptgps.utils.ApplicationUtil;

public class PersonCenterActivity extends AppCompatActivity implements View.OnClickListener{
  private EditText modifyUsername, modifyPass, modifyRePass;
  private Button modifyBtn, quitBtn;
  private LinearLayout passLinear, rePassLinear;
  private RadioGroup genderGroup;
  private RadioButton boy,girl;
  private static SharedPreferences mSharedPreferences;
  private ImageView headpic;
  private String sex = "男";
  private Handler mHandler = new Handler();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_person_center);
    mSharedPreferences = ApplicationUtil.getApplication().getSharedPreferences("BuptGPS", MODE_PRIVATE);
    init();
    genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i){
          case R.id.boy2:
          sex = "男";
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              headpic.setImageResource(R.drawable.male_avatar);
            }
          });
          break;
          case R.id.girl2:
            sex = "女";
            mHandler.post(new Runnable() {
              @Override
              public void run() {
               headpic.setImageResource(R.drawable.female_avatar);
              }
            });
            break;
        }
      }
    });
  }

  public void init(){
    headpic = (ImageView) findViewById(R.id.imageview1);
    modifyUsername = (EditText) findViewById(R.id.modify_username);
    modifyUsername.setText(mSharedPreferences.getString("userName", ""));
    modifyPass = (EditText) findViewById(R.id.modify_password);
    modifyPass.setText(mSharedPreferences.getString("password", ""));
    modifyRePass = (EditText) findViewById(R.id.modify_repassword);
    modifyRePass.setText(mSharedPreferences.getString("password",""));
    genderGroup = (RadioGroup) findViewById(R.id.gendergroup2);
    passLinear = (LinearLayout) findViewById(R.id.pass_linear);
    rePassLinear = (LinearLayout) findViewById(R.id.repass_linear);
    modifyBtn = (Button) findViewById(R.id.modify_button);
    modifyBtn.setOnClickListener(this);
    quitBtn = (Button) findViewById(R.id.quit_button);
    quitBtn.setOnClickListener(this);
    boy = (RadioButton) findViewById(R.id.boy2);
    girl = (RadioButton) findViewById(R.id.girl2);

    if(mSharedPreferences.getString("gender","0").equals("0")){
      headpic.setImageResource(R.drawable.male_avatar);
      boy.setChecked(true);
      girl.setChecked(false);
    }else{
      boy.setChecked(false);
      girl.setChecked(true);
      headpic.setImageResource(R.drawable.female_avatar);
    }
  }
  @Override
  public void onClick(View view) {
    switch (view.getId()){
      case R.id.quit_button:
        Intent intent=new Intent(PersonCenterActivity.this,LoginActivity.class);
        startActivity(intent);
        PersonCenterActivity.this.finish();
        break;
      case R.id.modify_button:
        if(modifyBtn.getText().equals("修改资料")){
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              modifyUsername.setEnabled(true);
              boy.setEnabled(true);
              girl.setEnabled(true);
              passLinear.setVisibility(View.VISIBLE);
              rePassLinear.setVisibility(View.VISIBLE);
              modifyBtn.setText("确定");
            }
          });
        }else{
          if(modifyRePass.getText().toString().equals(modifyPass.getText().toString())){
            if(savePersonnalInfo()){
              Toast.makeText(PersonCenterActivity.this, "修改资料成功！", Toast.LENGTH_SHORT).show();
            }else {
              Toast.makeText(PersonCenterActivity.this, "修改资料失败！", Toast.LENGTH_SHORT).show();
            }
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                modifyUsername.setEnabled(false);
                boy.setEnabled(false);
                girl.setEnabled(false);
                passLinear.setVisibility(View.GONE);
                rePassLinear.setVisibility(View.GONE);
                modifyBtn.setText("修改资料");
              }
            });
          }else {
            modifyRePass.setError("两次密码不一致");
          }
        }
    }
  }

  private boolean savePersonnalInfo(){
    SharedPreferences.Editor editor = mSharedPreferences.edit();
    editor.putString("userName", modifyUsername.getText().toString());
    editor.putString("password", modifyPass.getText().toString());
    editor.putString("gender", sex.equals("男") ? "0" : "1");
    return editor.commit();
  }
}
