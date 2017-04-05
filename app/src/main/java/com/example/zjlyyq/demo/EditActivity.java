package com.example.zjlyyq.demo;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.zjlyyq.demo.Adapter.PhotoAdapter;
import com.example.zjlyyq.demo.models.User;

import java.io.FileNotFoundException;

/**
 * Created by jialuzhang on 2017/3/30.
 */

public class EditActivity extends AppCompatActivity {
    EditText username_edit;
    EditText userage_edit;
    EditText usersex_edit;
    ImageView photo;
    ImageButton commit_change;
    private User user;
    private  int user_id;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        ContentResolver cr = this.getContentResolver();
        try {
            Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
            photo.setImageBitmap(bmp);
            user.setUserPhoto(bmp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        //Log.d("TEST2","编辑界面");
        user_id = getIntent().getIntExtra("userId",-1);
        Log.d("TESTEDIT","用户id是:"+user_id);
        User user2 = new User(this);
        user = user2.getUserById(user_id);
        userage_edit = (EditText)findViewById(R.id.age_edit);
        usersex_edit = (EditText)findViewById(R.id.sex_edit);
        username_edit = (EditText)findViewById(R.id.userName_edit);
        photo = (ImageView)findViewById(R.id.photo_view);
        commit_change = (ImageButton)findViewById(R.id.commit_edit);
        if (user.getUserPhoto()!=null){
            photo.setImageBitmap(user.getUserPhoto());
        }
        if (user.getAge() == -1){
            userage_edit.setText("未知");
        }else{
            String age = ""+user.getAge();
            userage_edit.setText(age);
        }
        if (user.getSex()==0){
            usersex_edit.setText("不详");
        }else {
            usersex_edit.setText(user.getSex() == 1?"男":"女");
        }
        username_edit.setText(user.getUserName());
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");   //说明要选择的内容是图片
                intent.putExtra("return-data", true); //要返回内容
                startActivityForResult(intent,1);
            }
        });
        commit_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age;
                if (userage_edit.getText().toString().length()>0){
                    age = Integer.parseInt(userage_edit.getText().toString());
                }else {
                    age = -1;
                }
                user.setAge(age);
                user.setUserName(username_edit.getText().toString());
                user.updateDatabase();
                EditActivity.this.finish();
            }
        });
    }



}
