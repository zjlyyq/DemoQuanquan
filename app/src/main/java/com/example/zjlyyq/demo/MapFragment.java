package com.example.zjlyyq.demo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.zjlyyq.demo.Adapter.ListViewAdapter;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageUnion;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jialuzhang on 2017/3/19.
 */

public class MapFragment extends Fragment implements LocationSource,AMapLocationListener {
    private MapView mapView = null;
    private ImageButton user_bt;
    private AMap aMap = null;
    private ImageButton publish_button;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private ListView listView;
    private ArrayList<Message> messages;
    private ArrayList<User> users;
    private MessageUnion messageUnion;
    private ListViewAdapter mListViewAdapter;


    //private SQLiteDatabase db;
    //private ArrayList<ArrayList<HashMap<String,Object>>> mArrayList;
    LinearLayout opt;
    ImageButton btSelect;
    ImageButton btTalk;
    ImageView iv;
    Bitmap baseBitmap;
    Canvas canvas;
    Paint paint;
    private int userId;
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    public static MapFragment newInstance(int user_id){
        Bundle argc = new Bundle();
        argc.putInt("userId",user_id);
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(argc);
        return mapFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle argc = getArguments();
        userId = argc.getInt("userId");
        Toast.makeText(getActivity(),"欢迎回到圈圈,你的编号是:"+userId,Toast.LENGTH_LONG).show();
        //获取状态的集合
        messageUnion = MessageUnion.getInstance(getActivity());
        users = new ArrayList<User>();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main,container,false);
        toFindEverything(view);
        initMap();
        mapView.onCreate(savedInstanceState);
        Toast.makeText(getActivity(),"欢迎回到圈圈,你的编号是:"+userId,Toast.LENGTH_LONG).show();
        return view;
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
        mLocationClient = new AMapLocationClient(getActivity());
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
                Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_LONG).show();
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
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(),"欢迎回到圈圈,你的编号是:"+userId,Toast.LENGTH_LONG).show();
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mapView.onResume();
        //messages = messageUnion.getMessages();
        new MyAsyncTask().execute();
        //com.example.zjlyyq.demo.Adapter.ListViewAdapter adapter = new com.example.zjlyyq.demo.Adapter.ListViewAdapter(getActivity(),messages,getActivity().getSupportFragmentManager());
        //listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEST2","开始跳转1");
                Intent intent = new Intent(getActivity(),MessageDetailActivity.class);
                Message message = messages.get(position);
                intent.putExtra("MESSAGEID", message.getMessageId());
                Log.d("TEST2","开始跳转");
                startActivity(intent);
            }
        });
        publish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PublishActivity.class);
                intent.putExtra("USERID",userId);
                startActivity(intent);
            }
        });

        user_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UserHomeActivity.class);
                intent.putExtra("userId",userId);
                Log.d("TEST2","穿过去的id是："+userId);
                startActivity(intent);
            }
        });
        opt.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        //长按显示去区域内所有的动态
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
                    Toast.makeText(getActivity(), "请在地图上圈出你想要的区域", Toast.LENGTH_LONG).show();
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
                opt.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.stopLocation();//停止定位
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void toFindEverything(View view){
        publish_button = (ImageButton) view.findViewById(R.id.publish_button);
        user_bt = (ImageButton)view.findViewById(R.id.userHome_bt);
        iv = (ImageView)view.findViewById(R.id.ivDraw);
        mapView = (MapView) view.findViewById(R.id.map);
        listView = (ListView)view.findViewById(R.id.listView1);
        opt = (LinearLayout)view.findViewById(R.id.options);
        btSelect = (ImageButton) view.findViewById(R.id.btSelect);
        btTalk = (ImageButton)view.findViewById(R.id.btTalk);
    }
    private class MyAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            getMessages();
            try {
                getUsers();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            com.example.zjlyyq.demo.Adapter.ListViewAdapter adapter = new com.example.zjlyyq.demo.Adapter.ListViewAdapter(getActivity(),messages,users,getActivity().getSupportFragmentManager());
            listView.setAdapter(adapter);
        }
    }
    public  void getMessages(){
        Map<String,Object> messagemap = new HashMap<String,Object>();
        messagemap.put("lx",-1);
        messagemap.put("ly",-1);
        messagemap.put("rx",1);
        messagemap.put("ry",1);
        JSONObject jsonObject = new JSONObject(messagemap);
        messages = new ArrayList<Message>();
        Log.d("TEST","开始查询");
        Log.d("TEST",jsonObject.toString());
        HttpTool httpTool = new HttpTool("GetMessages",jsonObject);
        String result = null;
        try {
            result = httpTool.jsonResult();
            JSONObject messageUnion = new JSONObject(result);
            int count = messageUnion.getInt("count");
            for (int i = 0;i < count;i ++){
                JSONObject jsonObject1 = messageUnion.getJSONObject("message"+i);
                Message message1 = null;
                try {
                    message1 = new Message(jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                messages.add(message1);
                //Log.d("TEST",jsonObject1.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getUsers() throws JSONException, IOException {
        //ArrayList<User> users = new ArrayList<User>();
        Log.d("USER",""+messages.size());
        for (int i = 0;i < messages.size();i ++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",messages.get(i).getPublisherId());
            HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
            String result = httpTool.jsonResult();
            JSONObject user = new JSONObject(result);
            Log.d("USER",user.toString());
            User user1 = new User(jsonObject);
            users.add(user1);
        }
    }
}
