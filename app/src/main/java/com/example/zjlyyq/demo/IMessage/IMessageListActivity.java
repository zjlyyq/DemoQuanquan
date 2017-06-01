package com.example.zjlyyq.demo.IMessage;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.fragment.MessageFragment;
import com.example.zjlyyq.demo.fragment.MyFragmentAdapter;

import java.util.ArrayList;

public class IMessageListActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imessage_list);

        userId = getIntent().getIntExtra("userId",-1);
        initView();
        initData();
    }

    public void initView(){
        toolbar = (Toolbar)findViewById(R.id.imessage_list_toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)this;
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        appCompatActivity.setTitle("我的消息");
        tabLayout = (TabLayout)findViewById(R.id.imessage_list_tabs);
        viewPager = (ViewPager)findViewById(R.id.imessage_list_viewpager);
    }

    public void initData(){
        ArrayList<Fragment> list = new ArrayList<Fragment>();
        list.add(new Fragment());
        list.add(new Fragment());
        list.add(new Fragment());
        list.add(new Fragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
