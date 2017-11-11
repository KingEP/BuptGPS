package com.kingep.buptsse.buptgps.bean;

/**
 * Created by enpeng.wang on 2017/11/11.
 */

public class Comment {
  private String userName;
  private String commentTime;
  private String gender;
  private String commentString;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getCommentTime() {
    return commentTime;
  }

  public void setCommentTime(String commentTime) {
    this.commentTime = commentTime;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getCommentString() {
    return commentString;
  }

  public void setCommentString(String commentString) {
    this.commentString = commentString;
  }
}
