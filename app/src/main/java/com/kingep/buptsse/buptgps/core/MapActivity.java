package com.kingep.buptsse.buptgps.core;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.kingep.buptsse.buptgps.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class MapActivity extends Activity implements SensorEventListener,
    OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

  // 定位相关
  LocationClient mLocClient;
  public MyLocationListenner myListener = new MyLocationListenner();
  private LocationMode mCurrentMode;
  BitmapDescriptor mCurrentMarker;
  private static final int accuracyCircleFillColor = 0xAAFFFF88;
  private static final int accuracyCircleStrokeColor = 0xAA00FF00;
  private SensorManager mSensorManager;
  private Double lastX = 0.0;
  private int mCurrentDirection = 0;
  private double mCurrentLat = 0.0;
  private double mCurrentLon = 0.0;
  private float mCurrentAccracy;
  private static int roadFlag = 0;

  private List<String> suggest;
  private ArrayAdapter<String> sugAdapter = null;

  MapView mMapView;
  BaiduMap mBaiduMap;

  // UI相关
  OnCheckedChangeListener radioButtonListener;
  Button requestLocButton;
  boolean isFirstLoc = true; // 是否首次定位
  private MyLocationData locData;
  private float direction;
  private ImageButton road_btn, navi_btn, fix_pos_btn, search_btn;
  private SuggestionSearch mSuggestionSearch = null;

  private AutoCompleteTextView search_text = null;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SDKInitializer.initialize(getApplicationContext());
    mLocClient = new LocationClient(this);
    mLocClient.registerLocationListener(myListener);

    setContentView(R.layout.activity_map);
    init();
    OnClickListener btnClickListener = new OnClickListener() {
      public void onClick(View v) {
        switch (mCurrentMode) {
          case NORMAL:
            v.post(new Runnable() {
              @Override
              public void run() {
                navi_btn.setImageResource(R.drawable.navi_light);
              }
            });
            mCurrentMode = LocationMode.COMPASS;
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.overlook(0);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            break;
          case COMPASS:
            v.post(new Runnable() {
              @Override
              public void run() {
                navi_btn.setImageResource(R.drawable.navi);
              }
            });
            mCurrentMode = LocationMode.NORMAL;
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
            MapStatus.Builder builder1 = new MapStatus.Builder();
            builder1.overlook(0);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
            break;
          default:
            break;
        }
      }
    };

    road_btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (roadFlag % 2 == 0) {
          roadFlag++;
          view.post(new Runnable() {
            @Override
            public void run() {
              road_btn.setImageResource(R.drawable.road_light);
              mBaiduMap.setTrafficEnabled(true);
            }
          });
        } else {
          roadFlag++;
          view.post(new Runnable() {
            @Override
            public void run() {
              road_btn.setImageResource(R.drawable.road);
              mBaiduMap.setTrafficEnabled(false);
            }
          });
        }
      }
    });

    fix_pos_btn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
      }
    });

    search_text.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() <= 0) {
          return;
        }

        mSuggestionSearch
            .requestSuggestion(new SuggestionSearchOption()
                .keyword(charSequence.toString()).city("北京"));
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    navi_btn.setOnClickListener(btnClickListener);
    // 地图初始化
    mMapView = (MapView) findViewById(R.id.bmapView);
    mBaiduMap = mMapView.getMap();
    // 开启定位图层
    mBaiduMap.setMyLocationEnabled(true);
    // 定位初始化
    LocationClientOption option = new LocationClientOption();
    option.setOpenGps(true); // 打开gps
    option.setCoorType("bd09ll"); // 设置坐标类型
    option.setScanSpan(1000);
    mLocClient.setLocOption(option);
    mLocClient.start();
  }

  public void init() {
    road_btn = (ImageButton) findViewById(R.id.road_btn);
    road_btn.getBackground().setAlpha(150);
    navi_btn = (ImageButton) (findViewById(R.id.navi_btn));
    navi_btn.getBackground().setAlpha(150);
    fix_pos_btn = (ImageButton) findViewById(R.id.fix_pos_btn);
    fix_pos_btn.getBackground().setAlpha(150);
    search_btn = (ImageButton) findViewById(R.id.search_btn);
    search_btn.getBackground().setAlpha(150);
    search_text = (AutoCompleteTextView) findViewById(R.id.search_text);
    search_text.getBackground().setAlpha(150);
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
    mCurrentMode = LocationMode.NORMAL;
    // 初始化建议搜索模块，注册建议搜索事件监听
    mSuggestionSearch = SuggestionSearch.newInstance();
    mSuggestionSearch.setOnGetSuggestionResultListener(this);


    sugAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_dropdown_item_1line);
    search_text.setAdapter(sugAdapter);
    search_text.setThreshold(1);
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    double x = sensorEvent.values[SensorManager.DATA_X];
    if (Math.abs(x - lastX) > 1.0) {
      mCurrentDirection = (int) x;
      locData = new MyLocationData.Builder()
          .accuracy(mCurrentAccracy)
          // 此处设置开发者获取到的方向信息，顺时针0-360
          .direction(mCurrentDirection).latitude(mCurrentLat)
          .longitude(mCurrentLon).build();
      mBaiduMap.setMyLocationData(locData);
    }
    lastX = x;

  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int i) {

  }

  @Override
  public void onGetPoiResult(PoiResult poiResult) {

  }

  @Override
  public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

  }

  @Override
  public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

  }

  @Override
  public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
      return;
    }
    suggest = new ArrayList<String>();
    for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
      if (info.key != null) {
        suggest.add(info.key);
      }
    }
    sugAdapter = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_dropdown_item_1line, suggest);
    search_text.setAdapter(sugAdapter);
    sugAdapter.notifyDataSetChanged();
  }

  /**
   * 定位SDK监听函数
   */
  public class MyLocationListenner implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {
      // map view 销毁后不在处理新接收的位置
      if (location == null || mMapView == null) {
        return;
      }
      mCurrentLat = location.getLatitude();
      mCurrentLon = location.getLongitude();
      mCurrentAccracy = location.getRadius();
      locData = new MyLocationData.Builder()
          .accuracy(location.getRadius())
          // 此处设置开发者获取到的方向信息，顺时针0-360
          .direction(mCurrentDirection).latitude(location.getLatitude())
          .longitude(location.getLongitude()).build();
      mBaiduMap.setMyLocationData(locData);
      if (isFirstLoc) {
        isFirstLoc = false;
        LatLng ll = new LatLng(location.getLatitude(),
            location.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
      }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }
  }

  @Override
  protected void onPause() {
    mMapView.onPause();
    super.onPause();
  }

  @Override
  protected void onResume() {
    mMapView.onResume();
    super.onResume();
    //为系统的方向传感器注册监听器
    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
        SensorManager.SENSOR_DELAY_UI);
  }

  @Override
  protected void onStop() {
    //取消注册传感器监听
    mSensorManager.unregisterListener(this);
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    // 退出时销毁定位
    mLocClient.stop();
    // 关闭定位图层
    mBaiduMap.setMyLocationEnabled(false);
    mMapView.onDestroy();
    mMapView = null;
    mSuggestionSearch.destroy();
    super.onDestroy();
  }
}
