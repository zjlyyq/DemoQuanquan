package com.example.zjlyyq.demo.Login;

import android.support.v4.app.Fragment;

import com.example.zjlyyq.demo.SingleFragmentActivity;

/**
 * Created by jialuzhang on 2017/3/19.
 */

public class LoginActivity extends SingleFragmentActivity{
    @Override
    public Fragment FragmentInit() {
        return new WelcomeFragment();
    }
}
