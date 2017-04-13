package com.example.zjlyyq.demo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int GET_PHOTO = 2;
    private ArrayList<Bitmap> photos;
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
    private static String SqlFile = "quanquan.db3";
    private static SQLiteDatabase db;
    private static MySQLiteOpenHelper mySQLiteOpenHelper;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) return;
        if(requestCode == GET_PHOTO){
            Uri uri = data.getData();
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
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        user_id = getIntent().getIntExtra("USERID",-1);
        mySQLiteOpenHelper = new MySQLiteOpenHelper(this,SqlFile,null,1);
        message = new Message(this);
        toFindEverything();
        photos = new ArrayList<Bitmap>();
        emo_bt.setOnClickListener(this);
        photo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");   //说明要选择的内容是图片
                intent.putExtra("return-data", true); //要返回内容
                startActivityForResult(intent,GET_PHOTO);
            }
        });
        //发布
        publist_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText(emotionTextView.getText().toString());
                message.setPublishDate(new Date().getTime());
                message.setPublisherId(user_id);
                message.setX(0);
                message.setY(0);
                Log.d("TEST","startinsert");
                new Thread(){
                    @Override
                    public void run() {
                        int message_id = insertMessage();
                    }
                }.start();
                PublishActivity.this.finish();
            }
        });
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
        Log.d("TEST","startinsert");
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
        Log.d("TEST",jsonObject.toString());
        try {
            String u = "http://192.168.199.115:8080/Quanquan/InsertMessage";
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();conn.setRequestProperty("accept", "*/*");
            //System.out.println("连接");
            //Log.d("TEST","连接成功");
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
}
