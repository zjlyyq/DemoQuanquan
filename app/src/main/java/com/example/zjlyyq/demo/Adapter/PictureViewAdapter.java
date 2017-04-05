package com.example.zjlyyq.demo.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jialuzhang on 2017/3/29.
 */

public class PictureViewAdapter extends PagerAdapter {
    private List<View> list;
    private TextView textView;
    public PictureViewAdapter(List<View> list,TextView textView){
        this.list = list;
        this.textView = textView;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        textView.setText(""+(position+1)+"/"+getCount());
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}
