package com.kingep.buptsse.buptgps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhy.http.okhttp.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Intent intent = new Intent(MainActivity.this, MainWindowActivity.class);
    startActivity(intent);
    this.finish();
  }
}
