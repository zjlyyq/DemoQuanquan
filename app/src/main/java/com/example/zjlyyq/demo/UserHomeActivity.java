package com.example.zjlyyq.demo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        userMessageTv = (TextView)findViewById(R.id.textView6);
        userName = (TextView)findViewById(R.id.textView4);
        photo = (ImageView)findViewById(R.id.imageView);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123){
                    if (!user.getUserPhoto().equals("null")){
                        //photo.setImageBitmap(user.getUserPhoto());
                    }
                    userName.setText(user.getUserName());
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                try {
                    user = getUser(user_id);
                    Message msg = new Message();
                    msg.what = 0x123;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
    public User getUser(int userid) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",userid);
        HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
        String result = httpTool.jsonResult();
        JSONObject user = new JSONObject(result);
        User user1 = new User(user);
        Log.d("TEST",user.toString());
        return user1;
    }
    private class MyAsyncTask extends AsyncTask<Integer, Void, User> {
        @Override
        protected User doInBackground(Integer... integers) {
            for (Integer integer : integers){
                try {
                    return getUser(integer);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
        }
    }
}
