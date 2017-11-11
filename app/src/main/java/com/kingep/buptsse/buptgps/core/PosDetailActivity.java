package com.kingep.buptsse.buptgps.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosDetailActivity extends AppCompatActivity implements View.OnClickListener,OnGetShareUrlResultListener {
  private String posName;
  private RollPagerView mRollViewPager;
  private static List<Comment> mCommentList = new ArrayList<>();
  private String jsonString = "[{\"comment\":\"很好\",\"gender\":\"1\",\"password\":\"1\",\"phoneNumber\":\"1\",\"posName\":\"1\",\"remark1\":\"1\",\"remark2\":\"1\",\"time\":\"2017-11-11 20:35:18\",\"userID\":\"1\",\"userName\":\"张希\"},{\"comment\":\"哈哈\",\"gender\":\"0\",\"password\":\"la\",\"phoneNumber\":\"la\",\"posName\":\"1\",\"remark1\":\"la\",\"remark2\":\"la\",\"time\":\"2017-11-11 21:30:23\",\"userID\":\"2\",\"userName\":\"王恩鹏\"}]";
  private Button mButton;
  private EditText commentEdit;
  private static CommentAdapter commentAdapter;
  private static int num;
  private ShareUrlSearch mShareUrlSearch = null;
  private double mCurrentLat = 0.0;
  private double mCurrentLon = 0.0;
  private String mAddress = "";
  private String mUserName = "";
  private String mGender = "";

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

    SharedPreferences sharedPreferences = ApplicationUtil.getApplication().getSharedPreferences("BuptGPS", MODE_PRIVATE);
    mUserName = sharedPreferences.getString("userName", "");
    mGender = sharedPreferences.getString("gender", "0");
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
    switch (item.getItemId()){
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
      JSONObject jsonObject;
      JSONArray jsonArray = new JSONArray(jsonString);
      for (int i = 0; i < jsonArray.length(); i++) {
        jsonObject = jsonArray.getJSONObject(i);
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
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.comment_btn:
        if (!TextUtils.isEmpty(commentEdit.getText())){
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
  }

  @Override
  public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {

  }
}
