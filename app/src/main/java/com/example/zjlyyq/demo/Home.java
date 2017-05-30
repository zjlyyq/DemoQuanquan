package com.example.zjlyyq.demo;

import android.support.v4.app.Fragment;

import com.example.zjlyyq.demo.fragment.MessageFragment;

/**
 * Created by jialuzhang on 2017/4/16.
 */

public class Home extends SingleFragmentActivity {
    @Override
    public Fragment FragmentInit() {
        int userid = getIntent().getIntExtra("userId",-1);
        int myid = getIntent().getIntExtra("myId",-1);
        return UserHomeFragment.newInstance(userid,myid);
    }
}