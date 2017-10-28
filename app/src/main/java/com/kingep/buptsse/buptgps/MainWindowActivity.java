package com.kingep.buptsse.buptgps;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.kingep.buptsse.buptgps.core.MapActivity;
import com.kingep.buptsse.buptgps.core.PersonCenterActivity;


public class MainWindowActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener{
  private RadioGroup main_radiogroup;
  private RadioButton tab_icon_map, tab_icon_person_center;
  private TabHost tabhost;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_window);

    main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);

    tab_icon_map = (RadioButton) findViewById(R.id.tab_icon_map);
    tab_icon_person_center = (RadioButton) findViewById(R.id.tab_icon_center);

    //往TabWidget添加Tab
    tabhost = getTabHost();
    tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, MapActivity.class)));
    tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, PersonCenterActivity.class)));
    main_radiogroup.setOnCheckedChangeListener(this);
  }

  @Override
  public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

    // TODO Auto-generated method stub
    switch(i){
      case R.id.tab_icon_map:
        tabhost.setCurrentTab(0);
        tab_icon_map.setChecked(true);
        break;
      case R.id.tab_icon_center:
        tabhost.setCurrentTab(1);
        tab_icon_person_center.setChecked(true);
        break;
    }
  }
}
