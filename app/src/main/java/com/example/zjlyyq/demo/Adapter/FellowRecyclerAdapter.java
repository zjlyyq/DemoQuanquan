package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/6/1.
 */

public class FellowRecyclerAdapter extends RecyclerView.Adapter<FellowRecyclerAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<User> fellows;
    public FellowRecyclerAdapter(Context mContext,ArrayList<User> fellows){
        this.mContext = mContext;
        this.fellows = fellows;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fan_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = fellows.get(position);
        holder.textView.setText(user.getUserName());
        Glide.with(mContext).load(user.getUserPhoto()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return fellows.size();
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
