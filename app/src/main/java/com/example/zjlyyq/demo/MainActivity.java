package com.example.zjlyyq.demo;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity{
    @Override
    public Fragment FragmentInit() {
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId",-1);
        return MapFragment.newInstance(userId);
    }
}
