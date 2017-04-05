package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.emoText.FaceText;

import java.util.List;

/**
 * Created by jialuzhang on 2017/3/6.
 */

public class EmotionGridAdapter extends BaseAdapter {
    List<FaceText> list;
    Context mContext;

    public EmotionGridAdapter(Context context, List<FaceText> list){
        this.mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_emo,null);

            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        FaceText faceText = (FaceText)getItem(position);
        String key = faceText.text;
        key = key.substring(key.indexOf('[')+1,key.indexOf(']'));
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(key,"drawable",mContext.getPackageName()));
        viewHolder.imageView.setImageDrawable(drawable);
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
    }
}
