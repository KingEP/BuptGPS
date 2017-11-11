package com.kingep.buptsse.buptgps.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jude.rollviewpager.RollPagerView;
import com.kingep.buptsse.buptgps.R;
import com.kingep.buptsse.buptgps.adapter.CommentAdapter;
import com.kingep.buptsse.buptgps.adapter.RollViewAdapter;
import com.kingep.buptsse.buptgps.bean.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosDetailActivity extends AppCompatActivity implements View.OnClickListener {
  private String posName;
  private RollPagerView mRollViewPager;
  private static List<Comment> mCommentList = new ArrayList<>();
  private String jsonString = "[{\"comment\":\"很好\",\"gender\":\"1\",\"password\":\"1\",\"phoneNumber\":\"1\",\"posName\":\"1\",\"remark1\":\"1\",\"remark2\":\"1\",\"time\":\"2017-11-11\",\"userID\":\"1\",\"userName\":\"张希\"},{\"comment\":\"哈哈\",\"gender\":\"0\",\"password\":\"la\",\"phoneNumber\":\"la\",\"posName\":\"1\",\"remark1\":\"la\",\"remark2\":\"la\",\"time\":\"2017-11-10\",\"userID\":\"2\",\"userName\":\"王恩鹏\"}]";
  private Button mButton;
  private EditText commentEdit;
  private static CommentAdapter commentAdapter;
  private static int num;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_pos_detail);
    init();
  }

  public void init() {
    Intent intent = getIntent();
    posName = intent.getStringExtra("posName");
    mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);

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
        }
    }
  }

  private void createComment(String commentText) {
    Comment comment = new Comment();
    comment.setUserName("KingEP");
    comment.setGender("0");
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

}
