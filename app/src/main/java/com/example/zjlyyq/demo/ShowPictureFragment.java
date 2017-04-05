package com.example.zjlyyq.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjlyyq.demo.Adapter.PictureViewAdapter;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/29.
 */

public class ShowPictureFragment extends Fragment {
    private TextView picture_order;
    private ViewPager picture_viewpager;
    private ArrayList<Bitmap> pictures;
    private int currentIndex;
    public static ShowPictureFragment newInstance(ArrayList<Bitmap> pics,int index){
        ShowPictureFragment showPictureFragment = new ShowPictureFragment();
        showPictureFragment.pictures = pics;
        showPictureFragment.currentIndex = index;
        return showPictureFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.show_picture,container,false);
        picture_order = (TextView)view.findViewById(R.id.picture_order);
        picture_viewpager = (ViewPager)view.findViewById(R.id.picture_view_page);
        ArrayList<View> list = new ArrayList<View>();
        for (int i = 0;i < pictures.size();i ++){
            Bitmap bitmap = pictures.get(i);
            View view1 = View.inflate(getActivity(),R.layout.picture_item,null);
            ImageView imageView = (ImageView)view1.findViewById(R.id.picture_item);
            imageView.setImageBitmap(bitmap);
            list.add(view1);
        }
        picture_viewpager.setAdapter(new PictureViewAdapter(list,picture_order));
        //int currentIndex = 0;
        picture_viewpager.setCurrentItem(currentIndex);
        return view;
    }
}
