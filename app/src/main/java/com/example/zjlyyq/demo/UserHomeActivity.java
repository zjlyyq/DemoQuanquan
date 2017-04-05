package com.example.zjlyyq.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.example.zjlyyq.demo.models.User;

/**
 * Created by jialuzhang on 2017/3/16.
 */

public class UserHomeActivity extends AppCompatActivity{
    private  int user_id;
    private User user;
    TextView userName;
    ImageView photo;
    private Button edit_bt;
    private TextView userMessageTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_id = getIntent().getIntExtra("userId",-1);
        User user2 = new User(this);
        user = user2.getUserById(user_id);
        userMessageTv = (TextView)findViewById(R.id.textView6);
        userName = (TextView)findViewById(R.id.textView4);
        photo = (ImageView)findViewById(R.id.imageView);

        if (user.getUserPhoto()!=null){
            photo.setImageBitmap(user.getUserPhoto());
        }
        userName.setText(user.getUserName());
        edit_bt = (Button)findViewById(R.id.button5);
        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserHomeActivity.this,EditActivity.class);
                intent.putExtra("userId",user_id);
                Log.d("TEST2","传到编辑界面的id是："+user_id);
                startActivity(intent);
            }
        });
    }
}
