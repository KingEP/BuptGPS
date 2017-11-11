package com.kingep.buptsse.buptgps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingep.buptsse.buptgps.R;
import com.kingep.buptsse.buptgps.bean.Comment;

import java.util.List;

/**
 * Created by enpeng.wang on 2017/11/11.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
  private List<Comment> mCommentList;

  public CommentAdapter(List<Comment> commentList){
    mCommentList = commentList;
  }

  static class ViewHolder extends RecyclerView.ViewHolder{
    ImageView avatar;
    TextView userName, time, comentText;

    public ViewHolder(View itemView) {
      super(itemView);
      avatar = (ImageView) itemView.findViewById(R.id.avatar);
      userName = (TextView) itemView.findViewById(R.id.username_text);
      time = (TextView) itemView.findViewById(R.id.time_text);
      comentText = (TextView) itemView.findViewById(R.id.comment_text);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.comment_item, parent, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Comment comment = mCommentList.get(position);
    if(comment.getGender().equals("0")) {
      holder.avatar.setImageResource(R.drawable.male_avatar);
    }else{
      holder.avatar.setImageResource(R.drawable.female_avatar);
    }
    holder.comentText.setText(comment.getCommentString());
    holder.time.setText(comment.getCommentTime());
    holder.userName.setText(comment.getUserName());
  }

  @Override
  public int getItemCount() {
    return mCommentList.size();
  }
}
