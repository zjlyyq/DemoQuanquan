package com.example.zjlyyq.demo;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.Adapter.*;
import com.example.zjlyyq.demo.Adapter.GridViewAdapter;
import com.example.zjlyyq.demo.Tools.MessageTools;
import com.example.zjlyyq.demo.Tools.UserTools;
import com.example.zjlyyq.demo.Widget.CircleImageView;
import com.example.zjlyyq.demo.models.Comment;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class MessageDetail extends AppCompatActivity {
    CircleImageView circleImageView;      //头像
    TextView nickName;
    TextView contentText;
    TextView timeText;
    GridView gridView;
    Context mContext;
    Toolbar toolbar;
    CommentsRecyclerAdapter adapter;
    EditText comment_et;
    Button comment_pb;
    CollapsingToolbarLayout collapsingToolbarLayout;
    int message_id;
    int userId;
    private ArrayList<String> imagesUrl;
    public static ArrayList<User> commenters;
    public static ArrayList<Comment> comments;
    private Message message;
    private User user;
    private String messagejson = "";
    ArrayList<String> imageUrls;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail_layout);
        mContext = this;
        initView();
        initData();
        comment_pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setText(comment_et.getText().toString());
                comment.setUserId(userId);
                comments.add(comment);
                commenters.add(user);
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void initToolbar(){
        toolbar = (Toolbar)findViewById(R.id.message_toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
    }
    public void initData(){
        message_id = getIntent().getIntExtra("messageId",-1);
        userId = getIntent().getIntExtra("userId",-1);
        new MyAsyMessage().execute();    //获取动态
    }
    public void initView(){
        circleImageView = (CircleImageView)findViewById(R.id.header);
        nickName = (TextView)findViewById(R.id.name);
        timeText = (TextView)findViewById(R.id.publish_time);
        gridView = (GridView)findViewById(R.id.gridview);
        contentText = (TextView)findViewById(R.id.talk);
        recyclerView = (RecyclerView)findViewById(R.id.comment_recycle);
        comment_et = (EditText)findViewById(R.id.comment_et);
        comment_pb = (Button)findViewById(R.id.comment_publish);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbarLayout);
        initToolbar();
    }
    public void getComments(){

    }
    class  MyAsyMessage extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            messagejson = MessageTools.getMessageById(message_id);
            Log.d("TESTJSON",messagejson);
            try {
                message = new Message(new JSONObject(messagejson));
                int userId = message.getPublisherId();
                JSONObject userJson = UserTools.getUserById(userId);
                user = new User(userJson);
                imageUrls = new ArrayList<String>();
                imageUrls = MessageTools.getImagesById(message_id);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return  null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (user!=null){
                Glide.with(mContext).load(user.getUserPhoto()).into(circleImageView);
                Log.d("FANS","头像："+user.getUserPhoto());
                nickName.setText(user.getUserName());
                collapsingToolbarLayout.setTitle(user.getUserName());
            }
            if (message!=null){
                contentText.setText(message.getText());
                Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/yy.TTF"); //设置宋体
                contentText.setTypeface(typeface);
            }
            if (imageUrls != null){
                Log.d("TESTJSON","size = "+imageUrls.size());
                GalikGridViewAdapter adapter = new GalikGridViewAdapter(mContext,imageUrls);
                gridView.setAdapter(adapter);
            }
            new GetCommentAsy().execute();
        }
    }
    class GetCommentAsy extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            comments = null;
            comments = new ArrayList<>();
            comments = MessageTools.getCommentsById(message_id);
            commenters = null;
            commenters = new ArrayList<>();
            for (int i = 0;i < comments.size();i ++){
                JSONObject jsonObject = UserTools.getUserById(comments.get(i).getUserId());
                try {
                    User user = new User(jsonObject);
                    commenters.add(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new CommentsRecyclerAdapter(MessageDetail.this);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(MessageDetail.this);
            recyclerView.setLayoutManager(manager);
        }
    }
}
