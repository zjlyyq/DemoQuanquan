package com.example.zjlyyq.demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationSource,AMapLocationListener{
    private MapView mapView = null;
    private AMap aMap = null;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private static final String TAG = "GpsActivity";
    private ListView listView;
    private ListViewAdapter mListViewAdapter;
    private ArrayList<ArrayList<HashMap<String,Object>>> mArrayList;
    private String[] usernames = new String[]{"后防天团","心有山海","仓央嘉措","晃过天空","大帝出征，寸草不生","里村红叶 ","皇马大礼包保级队","董卓爱吃菜 ","眼神能杀人 ","欧洲活雷锋"};
    String str1 = "每个人的性格中，都有某些无法让人接受的部分，再美好的人也一样。所以不要苛求别人，也不要埋怨自己。与其埋怨，不如改变自己。管好自己的心，做好自己的事，比什么都强。人生无完美，曲折亦风景。";
    String str2 = "最近超火的一只鹿，据说只要看到的30秒内转. 走，2016年剩下的两个月就能行大运！！！据悉，这只白色的驯鹿并不是新的物种，只是因为基因变异而导致皮肤发白。当地流传着一种说法：白鹿有一种特殊意义，它是神圣的，并会带来幸运。。反正我是信了[耶]";
    String str3 = "总有个人会爱你不大的眼睛 不高的鼻梁 不完美的身材 不长的小短腿 不会下降的体重和你不要脸的性格";
    String str4 = "总有个人会爱你不大的眼睛 不高的鼻梁 不完美的身材 不长的小短腿 不会下降的体重和你不要脸的性格";
    String str5 = "最近超火的一只鹿，据说只要看到的30秒内转. 走，2016年剩下的两个月就能行大运！！！据悉，这只白色的驯鹿并不是新的物种，只是因为基因变异而导致皮肤发白。当地流传着一种说法：白鹿有一种特殊意义，它是神圣的，并会带来幸运。。反正我是信了[耶]";
    String str6 = "每个人的性格中，都有某些无法让人接受的部分，再美好的人也一样。所以不要苛求别人，也不要埋怨自己。与其埋怨，不如改变自己。管好自己的心，做好自己的事，比什么都强。人生无完美，曲折亦风景。";
    String str7 = "最近超火的一只鹿，据说只要看到的30秒内转. 走，2016年剩下的两个月就能行大运！！！据悉，这只白色的驯鹿并不是新的物种，只是因为基因变异而导致皮肤发白。当地流传着一种说法：白鹿有一种特殊意义，它是神圣的，并会带来幸运。。反正我是信了[耶]";
    String str8 = "总有个人会爱你不大的眼睛 不高的鼻梁 不完美的身材 不长的小短腿 不会下降的体重和你不要脸的性格";
    String str9 = "总有个人会爱你不大的眼睛 不高的鼻梁 不完美的身材 不长的小短腿 不会下降的体重和你不要脸的性格";
    String str10 = "最近超火的一只鹿，据说只要看到的30秒内转. 走，2016年剩下的两个月就能行大运！！！据悉，这只白色的驯鹿并不是新的物种，只是因为基因变异而导致皮肤发白。当地流传着一种说法：白鹿有一种特殊意义，它是神圣的，并会带来幸运。。反正我是信了[耶]";
    private String[] desc = new String[]{str1,str2,str3,str4,str5,str6,str7,str8,str9,str10};
    private int[] imagesId = new int[]{R.drawable.touxiang,R.drawable.touxiang2,R.drawable.touxiang3,R.drawable.touxiang4,R.drawable.touxiang5,
            R.drawable.touxiang6,R.drawable.touxiang7,R.drawable.touxiang8,R.drawable.touxiang9,R.drawable.touxiang10};
    LinearLayout opt;
    ImageButton btSelect;
    ImageButton btTalk;
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
        //通知栏的同色设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.top_bg_color);//通知栏所需颜色
        }
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
                }
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
        Log.d(TAG, "destory");
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
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
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
    private void query(){
        List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
        for(int i = 0;i < usernames.length;i ++){
            Map<String,Object> listItem = new HashMap<String,Object>();
            listItem.put("header",imagesId[i]);
            listItem.put("personName",usernames[i]);
            listItem.put("talk",desc[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,listItems,R.layout.simple_item,
                new String[]{"personName","header","talk"},new int[]{R.id.name,R.id.header,R.id.talk});
        listView.setAdapter(simpleAdapter);
        //mapView.setAlpha(View.ALPHA.get(0.2));
    }

    private void query2(){
        initData();
        mListViewAdapter = new ListViewAdapter(mArrayList,MainActivity.this);
        listView.setAdapter(mListViewAdapter);
    }

    private void initData(){
        mArrayList=new ArrayList<ArrayList<HashMap<String,Object>>>();
        HashMap<String, Object> hashMap=null;
        ArrayList<HashMap<String,Object>> arrayListForEveryItem;
        for (int i = 0; i < 10; i++) {
            arrayListForEveryItem =new ArrayList<HashMap<String,Object>>();
            hashMap=new HashMap<String, Object>();
            hashMap.put("header",imagesId[i]);
            arrayListForEveryItem.add(hashMap);
            hashMap=new HashMap<String, Object>();
            hashMap.put("username",usernames[i]);
            arrayListForEveryItem.add(hashMap);
            hashMap=new HashMap<String, Object>();
            hashMap.put("talk",desc[i]);
            arrayListForEveryItem.add(hashMap);
            for (int j = 0; j < 9; j++) {
                hashMap=new HashMap<String, Object>();
                hashMap.put("picture"+j,imagesId[j]);
                arrayListForEveryItem.add(hashMap);
            }
            mArrayList.add(arrayListForEveryItem);
        }
        Object object = mArrayList.get(0).get(0).get("header");
        //Integer image = (Integer)object;
        //Toast.makeText(getApplicationContext(), object.getClass().getName().toString() +image+"或者"+R.drawable.touxiang, Toast.LENGTH_LONG).show();
    }
}
