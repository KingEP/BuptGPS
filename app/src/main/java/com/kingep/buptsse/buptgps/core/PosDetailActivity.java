package com.kingep.buptsse.buptgps.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.jude.rollviewpager.RollPagerView;
import com.kingep.buptsse.buptgps.R;
import com.kingep.buptsse.buptgps.adapter.CommentAdapter;
import com.kingep.buptsse.buptgps.adapter.RollViewAdapter;
import com.kingep.buptsse.buptgps.bean.Comment;
import com.kingep.buptsse.buptgps.utils.ApplicationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosDetailActivity extends AppCompatActivity implements View.OnClickListener, OnGetShareUrlResultListener {
  private String posName;
  private RollPagerView mRollViewPager;
  private List<Comment> mCommentList = new ArrayList<>();
  private Button mButton;
  private EditText commentEdit;
  private CommentAdapter commentAdapter;
  private static int num;
  private ShareUrlSearch mShareUrlSearch = null;
  private double mCurrentLat = 0.0;
  private double mCurrentLon = 0.0;
  private String mAddress = "";
  private String mUserName = "";
  private String mGender = "";
  private JSONArray mJSONArray;
  private String mString = "[{\"comment\":\"哈哈哈\",\"gender\":\"1\",\"time\":\"2017-11-12 11:22:20\",\"userName\":\"边茹月\"},{\"comment\":\"哈哈哈\",\"gender\":\"1\",\"time\":\"2017-11-12 11:24:20\",\"userName\":\"边茹月\"}]";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pos_detail);
    init();
  }

  public void init() {
    Intent intent = getIntent();
    posName = intent.getStringExtra("posName");
    mCurrentLat = intent.getDoubleExtra("lat", 0.0);
    mCurrentLon = intent.getDoubleExtra("lon", 0.0);
    mAddress = intent.getStringExtra("address");
    mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);
    SharedPreferences sharedPreferences1 = ApplicationUtil.getApplication().getSharedPreferences("User", MODE_PRIVATE);
    String user = sharedPreferences1.getString("userName", "");
    SharedPreferences sharedPreferences = ApplicationUtil.getApplication().getSharedPreferences("BuptGPS" + user, MODE_PRIVATE);
    mUserName = sharedPreferences.getString("userName", "");
    mGender = sharedPreferences.getString("gender", "0");
    File file = new File(getFilesDir().getPath()+"/"+posName+".json");
    if(!file.exists()) {
      writeFileData(posName + ".json", mString);
    }
    //设置播放时间间隔
    mRollViewPager.setPlayDelay(2500);
    //设置透明度
    mRollViewPager.setAnimationDurtion(500);
    //设置适配器
    mRollViewPager.setAdapter(new RollViewAdapter(posName));

    initComment();
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);
    commentAdapter = new CommentAdapter(mCommentList);
    recyclerView.setAdapter(commentAdapter);
    linearLayoutManager.setReverseLayout(true);
    linearLayoutManager.setStackFromEnd(true);

    mButton = (Button) findViewById(R.id.comment_btn);
    mButton.setOnClickListener(this);

    commentEdit = (EditText) findViewById(R.id.comment_edit);

    mShareUrlSearch = ShareUrlSearch.newInstance();
    mShareUrlSearch.setOnGetShareUrlResultListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.pos_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
//      case R.id.route:
//        Toast.makeText(PosDetailActivity.this, "显示路线", Toast.LENGTH_SHORT).show();
//        Intent intent =new Intent(PosDetailActivity.this, RouteActivity.class);
//        startActivity(intent);
//        break;
      case R.id.share:
        LatLng latLng = new LatLng(mCurrentLat, mCurrentLon);
        mShareUrlSearch
            .requestLocationShareUrl(new LocationShareURLOption()
                .location(latLng).snippet("测试分享点")
                .name(posName));
      default:
    }
    return true;
  }

  public void initComment() {
    try {
      InputStreamReader isr = new InputStreamReader(getAssets().open("comment1.json"), "UTF-8");
      BufferedReader br = new BufferedReader(isr);
      String line;
      StringBuilder builder = new StringBuilder();
      while ((line = br.readLine()) != null) {
        builder.append(line);
      }
      br.close();
      isr.close();
      String result = readFileData(posName+".json");//builder读取了JSON中的数据。
      JSONObject jsonObject;
      mJSONArray = new JSONArray(result);
      for (int i = 0; i < mJSONArray.length(); i++) {
        jsonObject = mJSONArray.getJSONObject(i);
        Comment comment = new Comment();
        comment.setUserName(jsonObject.getString("userName"));
        comment.setCommentTime(jsonObject.getString("time"));
        comment.setGender(jsonObject.getString("gender"));
        comment.setCommentString(jsonObject.getString("comment"));
        mCommentList.add(i, comment);
        num = i;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.comment_btn:
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentEdit.getWindowToken(), 0) ;
        if (!TextUtils.isEmpty(commentEdit.getText())) {
          createComment(commentEdit.getText().toString());
          view.post(new Runnable() {
            @Override
            public void run() {
              commentEdit.setText("");
            }
          });
          Toast.makeText(PosDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
        }
        break;
      default:
    }
  }

  private void createComment(String commentText) {
    Comment comment = new Comment();
    comment.setUserName(mUserName);
    comment.setGender(mGender);
    comment.setCommentTime(getFormatDate());
    comment.setCommentString(commentText);
    mCommentList.add(++num, comment);
    commentAdapter.notifyDataSetChanged();
    Map<String, String> map = new HashMap<>();
    map.put("userName", mUserName);
    map.put("gender", mGender);
    map.put("time", getFormatDate());
    map.put("comment", commentText);
    JSONObject jsonObject = new JSONObject(map);
    try {
      mJSONArray.put(num, jsonObject);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    writeFileData(posName+".json", mJSONArray.toString());
  }

  public String getFormatDate() {
    Date date = new Date();
    long times = date.getTime();//时间戳
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = formatter.format(date);
    return dateString;
  }

  @Override
  public void onGetPoiDetailShareUrlResult(ShareUrlResult shareUrlResult) {

  }

  @Override
  public void onGetLocationShareUrlResult(ShareUrlResult shareUrlResult) {
    // 分享短串结果
    Intent it = new Intent(Intent.ACTION_SEND);
    it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过百度地图SDK与您分享一个位置: " + mAddress
        + " -- " + shareUrlResult.getUrl());
    it.setType("text/plain");
    startActivity(Intent.createChooser(it, "将短串分享到"));
    finish();
  }

  @Override
  public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {

  }

  public String readFileData(String fileName) {
    String res = "";
    try {
      FileInputStream fin = openFileInput(fileName);
      int length = fin.available();
      byte[] buffer = new byte[length];
      fin.read(buffer);
      fin.close();
      res = new String(buffer);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }
  public void writeFileData(String fileName,String message){
    try{
      FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);
      byte [] bytes = message.getBytes();
      fout.write(bytes);
      fout.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
}
