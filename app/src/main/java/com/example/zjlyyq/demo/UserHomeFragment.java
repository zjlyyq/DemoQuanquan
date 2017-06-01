package com.example.zjlyyq.demo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.Tools.UserTools;
import com.example.zjlyyq.demo.Widget.CircleImageView;
import com.example.zjlyyq.demo.fragment.MessageFragment;
import com.example.zjlyyq.demo.fragment.MyFragmentAdapter;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.zjlyyq.demo.R.id.usertablayout;

/**
 * Created by jialuzhang on 2017/3/30.
 */

public class UserHomeFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar mToolbar;
    TextView imessage_bt;
    private int user_id;
    private int my_id;
    private User user;
    CircleImageView headImage;
    TextView userName;
    TextView fansNum;
    TextView favorNum;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppCompatActivity appCompatActivity;
    public  static UserHomeFragment newInstance(int userid,int myid){
        UserHomeFragment userHomeFragment = new UserHomeFragment();
        Bundle argc = new Bundle();
        argc.putInt("userId",userid);
        argc.putInt("myId",myid);
        userHomeFragment.setArguments(argc);
        return userHomeFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = getArguments().getInt("userId");
        my_id = getArguments().getInt("myId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.user_layout,container,false);
        new Thread(){
            @Override
            public void run() {
                initView(v);
                initDate();
            }
        }.start();
        return v;
    }
    //初始化界面
    @TargetApi(Build.VERSION_CODES.M)
    void initView(View v){
        collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.collapsing_collaps_toolbar);
        imessage_bt = (TextView)v.findViewById(R.id.imessage_bt);
        mToolbar = (Toolbar)v.findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.back);
        appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(mToolbar);
        viewPager = (ViewPager)v.findViewById(R.id.user_viewPager);
        tabLayout = (TabLayout)v.findViewById(usertablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        userName = (TextView)v.findViewById(R.id.home_username);
        favorNum = (TextView)v.findViewById(R.id.favornum);
        fansNum = (TextView)v.findViewById(R.id.fansnum);
        headImage = (CircleImageView)v.findViewById(R.id.home_head);
        imessage_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),IMessageActivity.class);
                intent.putExtra("userId",user_id);
                intent.putExtra("myId",my_id);
                startActivity(intent);
            }
        });
    }
    //初始化数据
    void initDate(){
        ArrayList<Fragment> list = new ArrayList<Fragment>();
        MessageFragment messageFragment = new MessageFragment(getActivity(),user_id,getActivity().getSupportFragmentManager());
        list.add(messageFragment);
        list.add(new Fragment());
        MyFragmentAdapter adapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        new MyAsyGetUser().execute();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    //菜单
    class MyAsyGetUser extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject userjson = UserTools.getUserById(user_id);
            Log.d("MYLOG",userjson.toString());
            try {
                user = new User(userjson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Glide.with(getActivity()).load(user.getUserPhoto()).into(headImage);
            userName.setText(user.getUserName());
            //appCompatActivity.setTitle(user.getUserName());
            collapsingToolbarLayout.setTitle(user.getUserName());
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.mymenu,menu);
    }
}
