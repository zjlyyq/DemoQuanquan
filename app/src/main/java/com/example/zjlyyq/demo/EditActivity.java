package com.example.zjlyyq.demo;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.Adapter.PhotoAdapter;
import com.example.zjlyyq.demo.Widget.CircleImageView;
import com.example.zjlyyq.demo.models.User;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jialuzhang on 2017/3/30.
 */

public class EditActivity extends AppCompatActivity implements MyPopupWindow.OnSelectedListener{
    private final String TAG = "UPLOAD";
    private final String MYURL = "http://192.168.43.38:8080/Quanquan";
    private final Context mContext = EditActivity.this;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final int GALLERY_REQUEST_CODE = 0;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 1;    // 相机拍照标记
    private AlertDialog mAlertDialog;
    TextView username_edit;
    TextView userage_edit;
    TextView usersex_edit;
    CircleImageView photo;
    ImageButton commit_change;
    Toolbar toolbar;
    private User user;
    private  int user_id;
    MyPopupWindow myPopupWindow;
    Uri imageUri;
    UCrop uCrop;
    Uri destUri;
    File outputImage ;
    File tempoutputImage;
    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri mDestinationUri;
    public void startCropActivity(Uri uri){
        Log.d(TAG, "Uri =  " + uri.toString());
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE){
            File temp = new File(mTempPhotoPath);
            startCropActivity(Uri.fromFile(temp));
        }
        else if (requestCode == GALLERY_REQUEST_CODE){
            startCropActivity(data.getData());
        }
        else if (requestCode == UCrop.REQUEST_CROP){
            destUri = UCrop.getOutput(data);
            Log.d(TAG, "Uri =  " + destUri.toString());
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(destUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            photo.setImageBitmap(bitmap);
            String filePath = destUri.getEncodedPath();
            final String imagePath = Uri.decode(filePath);
            uploadImage(imagePath);
        }
    }
    public void initToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar1);
        toolbar.setNavigationIcon(R.mipmap.back);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        initToolbar();
        AppCompatActivity appCompatActivity = (AppCompatActivity)this;
        appCompatActivity.setSupportActionBar(toolbar);
        user_id = getIntent().getIntExtra("userId",-1);
        mDestinationUri = Uri.fromFile(new File(this.getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        photo = (CircleImageView)findViewById(R.id.photo_view);
        Glide.with(this).load(MapFragment.imageUrl).into(photo);
        myPopupWindow = new MyPopupWindow(EditActivity.this);
        myPopupWindow.setOnSelectedListener(this);
    }
    public void  ChangePhoto(View source){
        //Toast.makeText(EditActivity.this,"开始编辑",Toast.LENGTH_LONG).show();
        myPopupWindow.showPopupWindow(this);
    }
    public User getUser(int userid) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",userid);
        HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
        String result = httpTool.jsonResult();
        JSONObject user = new JSONObject(result);
        User user1 = new User(user);
        return user1;
    }

    @Override
    public void OnSelected(View v, int position) {
        switch (position) {
            case 0:
                // "拍照"按钮被点击了
                takePhoto1();
                break;
            case 1:
                // "从相册选择"按钮被点击了
                pickFromGallery();
                break;
            case 2:
                // "取消"按钮被点击了
                myPopupWindow.dismissPopupWindow();
                break;
        }
    }


    public void uploadImage(String imagePath){
        new NetworkTask().execute(imagePath);
    }

    class NetworkTask extends AsyncTask<String,Integer,String>{

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
                Map<String,Object> userImage = new HashMap<String,Object>();
                userImage.put("userId",user_id);
                userImage.put("imageUrl",imageUrl);
                Log.d("UPDATEIMAGE",userImage.toString());
                JSONObject userjson = new JSONObject(userImage);
                HttpTool httpTool = new HttpTool("UpdateUserPhoto",userjson);
                Log.d("UPDATEIMAGE",userjson.toString());
                httpTool.jsonResult();
                MapFragment.imageUrl = imageUrl;
                Log.d("IMAGETEST",""+1+imageUrl);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    private void takePhoto1() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            myPopupWindow.dismissPopupWindow();
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //下面这句指定调用相机拍照后的照片存储的路径
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
        }
    }
    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            myPopupWindow.dismissPopupWindow();
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
                    takePhoto1();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
