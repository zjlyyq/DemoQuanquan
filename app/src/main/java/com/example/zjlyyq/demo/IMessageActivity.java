package com.example.zjlyyq.demo;

import android.support.v4.app.Fragment;

/**
 * Created by jialuzhang on 2017/4/17.
 */

public class IMessageActivity extends SingleFragmentActivity {

    @Override
    public Fragment FragmentInit() {
        int userid = getIntent().getIntExtra("userId",-1);
        int myid = getIntent().getIntExtra("myId",-1);
        return IMessageFragment.newInstance(userid,myid);
    }
}
