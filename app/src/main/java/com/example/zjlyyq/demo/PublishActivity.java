package com.example.zjlyyq.demo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.zjlyyq.demo.Adapter.EmotionGridAdapter;
import com.example.zjlyyq.demo.Adapter.EmotoonPagerAdapter;
import com.example.zjlyyq.demo.Adapter.PhotoAdapter;
import com.example.zjlyyq.demo.Emotion.EmotionTextView;
import com.example.zjlyyq.demo.emoText.FaceText;
import com.example.zjlyyq.demo.emoText.FaceTextUtils;
import com.example.zjlyyq.demo.models.Images;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageImage;
import com.example.zjlyyq.demo.models.MySQLiteOpenHelper;
import com.example.zjlyyq.demo.models.User;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener,AMapLocationListener{
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    protected static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 103;
    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记
    private AlertDialog mAlertDialog;
    private final String TAG = "UPLOAD";
    private static final int GET_PHOTO = 2;
    private ArrayList<Bitmap> photos;
    private ArrayList<Uri> imageUris;
    private ArrayList<String> imageUrls;

    int message_id = -1;
    UCrop uCrop;
    // 剪切后图像文件
    private Uri mDestinationUri;
    PhotoAdapter adapter;
    Button publist_bt;
    Button cencel_bt;
    Button emo_bt;
    ViewStub viewStub;
    ViewPager vpEmo;
    GridView photo_container;
    EmotionTextView emotionTextView;
    List<FaceText> emos;
    Button photo_bt;
    private Message message = null;
    private static int user_id;
    double x,y;
    /**
     * 定位相关
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if(requestCode == GALLERY_REQUEST_CODE){
            Uri uri = data.getData();
            Log.d(TAG, "Uri =  " + data.getData().toString());
            startCropActivity(data.getData());

            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                photos.add(bmp);
                adapter = new PhotoAdapter(this,photos);
                photo_container.setAdapter(adapter);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == UCrop.REQUEST_CROP){
            Uri destUri = UCrop.getOutput(data);
            Log.d(TAG, "destUri =  " + destUri.toString());
            imageUris.add(destUri);
            String filePath = destUri.getEncodedPath();
            final String imagePath = Uri.decode(filePath);
            uploadImage(imagePath);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        requestAcess();

        mDestinationUri = Uri.fromFile(new File(this.getCacheDir(), "cropImage.jpeg"));
        user_id = getIntent().getIntExtra("USERID",-1);
        message = new Message(this);
        toFindEverything();
        photos = new ArrayList<Bitmap>();
        imageUris = new ArrayList<Uri>();
        imageUrls = new ArrayList<String>();
        emo_bt.setOnClickListener(this);

        photo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 这里需要改进图片的上传方式，现在只能上传裁剪过的图片
                 */
                /*
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");   //说明要选择的内容是图片
                intent.putExtra("return-data", true); //要返回内容
                startActivityForResult(intent,GET_PHOTO);
                */
                pickFromGallery();
            }
        });
        //发布
        publist_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText(emotionTextView.getText().toString());
                message.setPublishDate(new Date().getTime());
                message.setPublisherId(user_id);
                Log.d("PUBLISH",emotionTextView.getText().toString());
                initPosition();
                message.setX(x-0.0002);
                message.setY(y+0.0001);
                Log.d("TEST","startinsert userid="+user_id);
                new Thread(){
                    @Override
                    public void run() {
                        message_id = insertMessage();
                        if(message_id > 0){
                            for(int i = 0;i < imageUrls.size();i++){
                                String imageUrl = imageUrls.get(i);
                                Map<String,Object> messageImage = new HashMap<String,Object>();
                                messageImage.put("messageId",message_id);
                                messageImage.put("imageUrl",imageUrl);
                                JSONObject userjson = new JSONObject(messageImage);
                                HttpTool httpTool = new HttpTool("InsertMessageImage",userjson);
                                try {
                                    httpTool.jsonResult();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }.start();
                PublishActivity.this.finish();
            }
        });
    }
    /**
     * 动态申请定位权限
     */
    public void requestAcess(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
    }
    /**
     * 定位
     */
    public void initPosition(){
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
        AMapLocation location = mlocationClient.getLastKnownLocation();
        x = location.getLongitude();
        y = location.getLatitude();
        Log.d("Location","经纬度："+x+":"+y);
    }
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    public void toFindEverything(){
        emotionTextView = (EmotionTextView)findViewById(R.id.edit_view);
        emo_bt = (Button)findViewById(R.id.emo_bt);
        photo_bt = (Button)findViewById(R.id.photo_bt);
        photo_container = (GridView)findViewById(R.id.photo_container);
        cencel_bt = (Button)findViewById(R.id.cancel_bt);
        publist_bt = (Button)findViewById(R.id.publich_bt);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emo_bt:
                if(viewStub == null){
                    initEmoView();
                    return;
                }
                if(vpEmo.getVisibility() == View.VISIBLE){
                   // emotionTextView.setText("状态变为不可见");
                    vpEmo.setVisibility(View.GONE);
                }
                else {
                   // emotionTextView.setText("状态变为可见");
                    vpEmo.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    public void initEmoView(){
        viewStub = (ViewStub)findViewById(R.id.viewStub);
        vpEmo = (ViewPager) viewStub.inflate().findViewById(R.id.view_page);

        List<View> list_Views = new ArrayList<View>();
        emos = FaceTextUtils.faceTexts;
        for(int i = 0;i < 2;i ++){
            list_Views.add(getViews(i));
        }
        vpEmo.setAdapter(new EmotoonPagerAdapter(list_Views));
    }

    public View getViews(int i){
        View view = View.inflate(this,R.layout.emo_gridview,null);
        final GridView gridView = (GridView)view.findViewById(R.id.gridview);
        List<FaceText> list = new ArrayList<FaceText>();
        if(i == 0){
            list = emos.subList(0,21);
        }
        else{
            list = emos.subList(21,emos.size());
        }
        final EmotionGridAdapter emotionGridAdapter = new EmotionGridAdapter(PublishActivity.this,list);
        gridView.setAdapter(emotionGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FaceText faceText = (FaceText)emotionGridAdapter.getItem(position);
                String key = faceText.text.toString();
                try {
                    if (emotionTextView != null && !TextUtils.isEmpty(key)) {
                        int start = emotionTextView.getSelectionStart();	//取得光标所在的位置
                        CharSequence content = emotionTextView.getText().insert(start,key);
                        emotionTextView.setText(content);
                        // 重新定位光标位置
                        CharSequence info = emotionTextView.getText();
                        if (info instanceof Spannable) {
                            Spannable spanText = (Spannable) info;
                            Selection.setSelection(spanText,
                                    start + key.length());
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
        return view;
    }
    public int insertMessage(){
        Map<String,Object> messagemap = new HashMap<String,Object>();
        messagemap.put("text",message.getText());
        messagemap.put("userId",message.getPublisherId());
        messagemap.put("publish_time",message.getPublishDate());
        messagemap.put("x",message.getX());
        messagemap.put("y",message.getY());
        messagemap.put("transmitTimes",message.getTransmitTimes());
        messagemap.put("commentTimes",message.getCommentTimes());
        messagemap.put("favourTimes",message.getFavourTimes());
        message.setTransmitTimes(0);
        message.setCommentTimes(0);
        message.setFavourTimes(0);
        //JSONObject jsonObject = new JSONObject(messagemap);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(messagemap.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String u = HttpTool.BASE_URL + ":8080/Quanquan/InsertMessage";
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();conn.setRequestProperty("accept", "*/*");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            PrintWriter out = null;
            // 获取URLConnection对象对应的输出流
            Log.d("TEST","获取URLConnection对象对应的输出流");
            out = new PrintWriter(conn.getOutputStream());
            Log.d("TEST","获取URLConnection对象对应的输出流success");
            // 发送请求参数
            out.print("json="+jsonObject.toString());
            // flush输出流的缓冲
            out.flush();
            BufferedReader in;
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
            Log.d("TEST",result);
            int message_id = 0;
            if (result.equals("error")){
                message_id = -2;
                Log.d("TEST",""+message_id);
            }else {
                message_id = Integer.parseInt(result);
                Log.d("TEST",""+message_id);
            }
            out.close();
            in.close();
            return message_id;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -2;
    }
    public void startCropActivity(Uri uri){
        //Log.d(TAG, "Uri =  " + uri.toString());
        uCrop = UCrop.of(uri,mDestinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置可操作手势
        options.setAllowedGestures(UCropActivity.SCALE,UCropActivity.ROTATE,UCropActivity.ALL);
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1,1);
        uCrop.start(this);
    }
    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            //myPopupWindow.dismissPopupWindow();
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }
    }

    /**
     * 显示指定标题和信息的对话框
     *
     * @param title                         - 标题
     * @param message                       - 信息
     * @param onPositiveButtonClickListener - 肯定按钮监听
     * @param positiveText                  - 肯定按钮信息
     * @param onNegativeButtonClickListener - 否定按钮监听
     * @param negativeText                  - 否定按钮信息
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }
    /**
     * 请求权限
     *
     * 如果权限被拒绝过，则提示用户需要权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //takePhoto1();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void uploadImage(String imagePath){
        new NetworkTask().execute(imagePath);
    }

    class NetworkTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return doPost(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private String doPost(String imagePath) throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "error";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //这里演示添加用户ID
        builder.addFormDataPart("userId", String.valueOf(user_id));
        builder.addFormDataPart("image", imagePath,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)));

        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(HttpTool.BASE_URL +":8080/Quanquan/"+ "/FileUpload")
                .post(requestBody)
                .build();

        Log.d(TAG, "请求地址 " + HttpTool.BASE_URL +":8080/Quanquan"+ "/FileUpload");
        try{
            Response response = mOkHttpClient.newCall(request).execute();
            Log.d(TAG, "响应码 " + response.code());
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                Log.d(TAG, "响应体 " + resultValue);
                Log.d(TAG,"图片地址：" + HttpTool.BASE_URL +":8080/Quanquan/"+resultValue);
                String imageUrl =  HttpTool.BASE_URL +":8080/Quanquan"+resultValue;
                imageUrls.add(imageUrl);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
