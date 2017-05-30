package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.MapFragment;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;

/**
 * Created by jialuzhang on 2017/5/31.
 */

public class FansRecyclerAdapter extends RecyclerView.Adapter<FansRecyclerAdapter.ViewHolder>{
    private Context mContext;
    public FansRecyclerAdapter(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fan_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User fan = MapFragment.fans.get(position);
        holder.textView.setText(fan.getUserName());
        Glide.with(mContext).load(fan.getUserPhoto()).into(holder.imageView);
        Log.d("FANS","头像："+fan.getUserPhoto());
    }

    @Override
    public int getItemCount() {
        return MapFragment.fans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;     //头像
        TextView textView;       //用户名
        ImageView sex;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.fan_name);
            imageView=(ImageView)itemView.findViewById(R.id.fan_head);
            sex = (ImageView) itemView.findViewById(R.id.fans_sex);
        }
    }
}
