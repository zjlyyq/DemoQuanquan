package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/4/27.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private ArrayList<com.example.zjlyyq.demo.models.Message> messages;
    ArrayList<ArrayList<String>> imageUrlss;
    private ArrayList<User> users;
    private Context mContext;
    public RecyclerAdapter(Context context,ArrayList<com.example.zjlyyq.demo.models.Message> messages,ArrayList<User> users,ArrayList<ArrayList<String>> imageUrlss){
        this.mContext = context;
        this.messages = messages;
        this.users = users;
        this.imageUrlss = imageUrlss;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.simple_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        com.example.zjlyyq.demo.models.Message message = messages.get(position);
        holder.textView.setText(user.getUserName());
        holder.textView2.setText(message.getText());
        holder.imageView.setImageResource(R.drawable.touxiang4);
        GalikGridViewAdapter adapter = new GalikGridViewAdapter(this.mContext,imageUrlss.get(position));
        holder.gridView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;     //头像
        TextView textView;       //用户名
        TextView textView2;     //动态正文
        GridView gridView;      //动态图片
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.name);
            textView2=(TextView) itemView.findViewById(R.id.talk);
            imageView=(ImageView)itemView.findViewById(R.id.header);
            gridView = (GridView) itemView.findViewById(R.id.gridview);
        }
    }
}
