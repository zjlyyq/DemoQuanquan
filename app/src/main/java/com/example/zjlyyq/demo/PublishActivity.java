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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                //将message插入数据库后返回messageid，用于插入messageImages数据表
                int message_id = message.insertIntoDatabase();
                for (int i = 0;i < photos.size();i ++){
                    Images images = new Images(PublishActivity.this);
                    images.setBitmap(photos.get(i));
                    //将images插入数据库后返回image_id，用于插入messageImages数据表
                    int image_id = images.insertIntoDatabase();
                    if(image_id != -1 && message_id != -1){
                        MessageImage messageImage = new MessageImage(PublishActivity.this,message_id,image_id);
                        messageImage.insertIntoDatabase();
                    }
                }
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
}
