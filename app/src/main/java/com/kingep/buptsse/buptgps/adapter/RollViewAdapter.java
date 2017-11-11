package com.kingep.buptsse.buptgps.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.kingep.buptsse.buptgps.R;

/**
 * Created by enpeng.wang on 2017/11/11.
 */

public class RollViewAdapter extends StaticPagerAdapter {
  private String posName;
  public RollViewAdapter(String posName) {
    this.posName = posName;
  }

  private int[] imgs = {
      R.drawable.img1,
      R.drawable.img2,
      R.drawable.img3,
      R.drawable.img4,
  };
  @Override
  public View getView(ViewGroup container, int position) {
    ImageView view = new ImageView(container.getContext());
    view.setImageResource(imgs[position]);
    view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    return view;
  }

  @Override
  public int getCount() {
    return imgs.length;
  }
}
