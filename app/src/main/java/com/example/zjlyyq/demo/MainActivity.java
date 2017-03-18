package com.example.zjlyyq.demo;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageUnion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationSource,AMapLocationListener{
    private MapView mapView = null;
    private ImageButton user_bt;
    private AMap aMap = null;
    private ImageButton publish_button;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private static final String TAG = "GpsActivity";
    private ListView listView;
    private ArrayList<Message> messages;
    private ListViewAdapter mListViewAdapter;
    private ArrayList<ArrayList<HashMap<String,Object>>> mArrayList;
    private int flag[] = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    private int px[] = new int[]{100,200,300,400,500,600,700,800,900,1000,1100,1200,50,150,250,350,450,550,650,750};
    private int py[] = new int[]{100,300,500,700,900,1100,1300,1500,1700,1900,100,300,500,700,900,1100,1300,1500,1700,1900};
    private int[] usernames = new int[]{R.string.name1,R.string.name2,R.string.name3,R.string.name4,R.string.name5,R.string.name6,R.string.name7
            ,R.string.name8,R.string.name9,R.string.name10,
            R.string.name11,R.string.name12,R.string.name13,R.string.name14,R.string.name15,R.string.name16,R.string.name17
            ,R.string.name18,R.string.name19,R.string.name20};
    private int[] imagesId = new int[]{R.drawable.touxiang,R.drawable.touxiang2,R.drawable.touxiang3,R.drawable.touxiang4,R.drawable.touxiang5,
            R.drawable.touxiang6,R.drawable.touxiang7,R.drawable.touxiang8,R.drawable.touxiang9,R.drawable.touxiang10,
            R.drawable.touxiang,R.drawable.touxiang2,R.drawable.touxiang3,R.drawable.touxiang4,R.drawable.touxiang5,
            R.drawable.touxiang6,R.drawable.touxiang7,R.drawable.touxiang8,R.drawable.touxiang9,R.drawable.touxiang10};
    LinearLayout opt;
    ImageButton btSelect;
    ImageButton btTalk;
    ImageView iv;
    Bitmap baseBitmap;
    Canvas canvas;
    Paint paint;
    private int[] talk = new int[]{R.string.talk1,R.string.talk2,R.string.talk3,R.string.talk4
    ,R.string.talk5,R.string.talk6,R.string.talk7,R.string.talk8,R.string.talk9,R.string.talk10,
            R.string.talk11,R.string.talk12,R.string.talk13,R.string.talk14
            ,R.string.talk15,R.string.talk16,R.string.talk17,R.string.talk18,R.string.talk19,R.string.talk20};
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messages = MessageUnion.getInstance(this).getMessages();
        //通知栏的同色设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.top_bg_color);//通知栏所需颜色
        }
        publish_button = (ImageButton) findViewById(R.id.publish_button);
        publish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PublishActivity.class);
                startActivity(intent);
            }
        });
        user_bt = (ImageButton)findViewById(R.id.userHome_bt);
        user_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserHomeActivity.class);
                startActivity(intent);
            }
        });
        iv = (ImageView)findViewById(R.id.ivDraw);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        listView = (ListView)findViewById(R.id.listView1);
        opt = (LinearLayout)findViewById(R.id.options);
        opt.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        btSelect = (ImageButton) findViewById(R.id.btSelect);
        btTalk = (ImageButton)findViewById(R.id.btTalk);
        btSelect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                opt.setVisibility(View.VISIBLE);
                return true;            //return true可以取消长按时的短按触发
            }
        });

        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opt.getVisibility() == View.VISIBLE){
                    opt.setVisibility(View.INVISIBLE);
                }
                if(listView.getVisibility() == View.VISIBLE){
                    listView.setVisibility(View.INVISIBLE);
                }
                else if(opt.getVisibility() == View.INVISIBLE && listView.getVisibility() == View.INVISIBLE){
                    Toast.makeText(getApplicationContext(), "请在地图上圈出你想要的区域", Toast.LENGTH_LONG).show();

                    // 创建一张空白图片
                    baseBitmap = Bitmap.createBitmap(1080,1920,Bitmap.Config.ARGB_8888);
                    // 创建一张画布
                    canvas = new Canvas(baseBitmap);
                    // 画布背景为灰色
                   // canvas.drawColor(Color.GRAY);
                    // 创建画笔
                    paint = new Paint();
                    // 画笔颜色为红色
                    paint.setColor(Color.RED);
                    // 宽度5个像素
                    paint.setStrokeWidth(5);
                    // 先将灰色背景画上
                    canvas.drawBitmap(baseBitmap, new Matrix(), paint);
                    iv.setImageBitmap(baseBitmap);
                    paint.setStyle(Paint.Style.STROKE);
                    iv.setVisibility(View.VISIBLE);
                }
            }
        });
        iv.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;
            int maxX = 0;
            int minX = 10000;
            int minY = 10000;
            int maxY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // 获取手按下时的坐标
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        if(minX > startX){
                            minX = startX;
                        }
                        if(maxX < startX){
                            maxX = startX;
                        }
                        if(minY > startY){
                            minY = startY;
                        }
                        if(maxX < startY){
                            maxY = startY;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取手移动后的坐标
                        int stopX = (int) event.getX();
                        int stopY = (int) event.getY();
                        // 在开始和结束坐标间画一条线
                        canvas.drawLine(startX, startY, stopX, stopY, paint);
                        // 实时更新开始坐标
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        if(minX > startX){
                            minX = startX;
                        }
                        if(maxX < startX){
                            maxX = startX;
                        }
                        if(minY > startY){
                            minY = startY;
                        }
                        if(maxY < startY){
                            maxY = startY;
                        }
                        iv.setImageBitmap(baseBitmap);
                        break;
                    case MotionEvent.ACTION_UP:
                        paint.setColor(Color.BLUE);
                        canvas.drawRect((float)minX,(float)minY,(float)maxX,(float)maxY,paint);
                        paint.setColor(Color.RED);
                        for(int i = 0;i < px.length;i ++){
                        if(px[i] < minX || px[i] > maxX){
                            flag[i] = 0;
                        }
                        if(py[i] < minY || py[i] > maxY){
                            flag[i] = 0;
                        }
                    }
                        query2();
                        listView.setVisibility(View.VISIBLE);
                        iv.setVisibility(View.INVISIBLE);
                        maxX = 0;
                        minX = 10000;
                        minY = 10000;
                        maxY = 0;
                        break;
                }
                return true;
            }
        });
        btTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                query2();
                opt.setVisibility(View.INVISIBLE);
            }
        });
        initMap();
    }
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final  int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if(on){
            winParams.flags |= bits;
        }else{
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mapView.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_NIGHT);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(200));
            //UiSettings 主要是对地图上的控件的管理，比如指南针、logo位置（不能隐藏）.....
            UiSettings settings = aMap.getUiSettings();
            //设置了定位的监听,这里要实现LocationSource接口
            aMap.setLocationSource(this);
            // 自定义系统定位小蓝点
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.user));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.argb(0,0,0,0));// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色

            aMap.setMyLocationStyle(myLocationStyle);
            //设置定位的类型
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            // 是否显示定位按钮
            settings.setMyLocationButtonEnabled(true);
            // 是否可触发定位并显示定位层
            aMap.setMyLocationEnabled(true);
        }
        initLocation();
    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // Date date = new Date(aMapLocation.getTime());
                //df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    //  aMap.moveCamera(CameraUpdateFactory.zoomTo(50));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);

                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                   // Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
            }
            else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void activate( LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
    }
    //停止定位
    public void deactivate() {
        mListener = null;
    }
    private void query2(){
        initData();
        if(mArrayList.isEmpty()){
            Toast.makeText(getApplicationContext(), "所选区域暂时没有动态", Toast.LENGTH_LONG).show();
            return;
        }
        mListViewAdapter = new ListViewAdapter(mArrayList,MainActivity.this);
        listView.setAdapter(mListViewAdapter);
        for(int i = 0;i < px.length;i ++){
            flag[i] = 1;
        }
    }
    private void initData(){
        mArrayList = new ArrayList<ArrayList<HashMap<String,Object>>>();
        HashMap<String, Object> hashMap=null;
        ArrayList<HashMap<String,Object>> arrayListForEveryItem;
        for (int i = 0; i < 20; i++) {
            if(flag[i] == 0){
                continue;
            }
            arrayListForEveryItem =new ArrayList<HashMap<String,Object>>();
            hashMap=new HashMap<String, Object>();
            hashMap.put("header",imagesId[i]);
            arrayListForEveryItem.add(hashMap);
            hashMap=new HashMap<String, Object>();
            hashMap.put("username",usernames[i]);
            arrayListForEveryItem.add(hashMap);
            hashMap=new HashMap<String, Object>();
            hashMap.put("talk",talk[i]);
            arrayListForEveryItem.add(hashMap);
            for (int j = 0; j < 9; j++) {
                hashMap=new HashMap<String, Object>();
                hashMap.put("picture"+j,imagesId[j]);
                arrayListForEveryItem.add(hashMap);
            }
            mArrayList.add(arrayListForEveryItem);
        }
    }
}
