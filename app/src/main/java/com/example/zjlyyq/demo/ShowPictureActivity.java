package com.example.zjlyyq.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/29.
 */

public class ShowPictureActivity extends SingleFragmentActivity {

    @Override
    public Fragment FragmentInit() {
        ArrayList<Bitmap> pictures = new ArrayList<Bitmap>();
        return ShowPictureFragment.newInstance(pictures,1);
    }
}
