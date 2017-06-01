package com.example.zjlyyq.demo.MyFavor;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.zjlyyq.demo.Adapter.ListViewAdapter;
import com.example.zjlyyq.demo.MapFragment;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.Tools.MessageTools;
import com.example.zjlyyq.demo.Tools.UserTools;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyFavorActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    private ArrayList<Message> messages;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favor);

        initView();
        initDate();
    }

    public void initView(){
        toolbar = (Toolbar)findViewById(R.id.myfavor_toolbar);
        listView = (ListView)findViewById(R.id.myfavour_listview);
        AppCompatActivity appCompatActivity = (AppCompatActivity)this;
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        appCompatActivity.setTitle("我的收藏");
    }

    public void initDate(){
        users = new ArrayList<>();
        messages = new ArrayList<>();
        new GetFavorMessages().execute();
    }

    class GetFavorMessages extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            Iterator<Integer> iterator = MapFragment.favorMessageIds.iterator();
            while (iterator.hasNext()){
                int id = iterator.next();
                Message message = MessageTools.getMessageByIdObject(id);
                messages.add(message);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new GetUsers().execute();
        }
    }

    class GetUsers extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0;i < messages.size();i ++)
            {
                User user = UserTools.getUserObjectById(messages.get(i).getPublisherId());
                users.add(user);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListViewAdapter listViewAdapter = new ListViewAdapter(MyFavorActivity.this,messages,users,getSupportFragmentManager(),null,null);
            listView.setAdapter(listViewAdapter);
        }
    }

}
