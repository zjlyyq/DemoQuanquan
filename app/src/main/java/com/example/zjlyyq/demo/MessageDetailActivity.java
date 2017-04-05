package com.example.zjlyyq.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jialuzhang on 2017/3/29.
 */

public class MessageDetailActivity  extends SingleFragmentActivity{

    @Override
    public Fragment FragmentInit() {
        return new MessageDetailFragment();
    }
}
