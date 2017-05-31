package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.MessageDetail;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.Widget.CircleImageView;
import com.example.zjlyyq.demo.models.Message;

/**
 * Created by jialu on 2017/5/31.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>{
    private Context mContext;
    public CommentsRecyclerAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText("刚刚");
        holder.name.setText(MessageDetail.commenters.get(position).getUserName());
        Glide.with(mContext).load(MessageDetail.commenters.get(position).getUserPhoto()).into(holder.header);
        holder.text.setText(MessageDetail.comments.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return MessageDetail.comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView header;     //头像
        TextView name;       //用户名
        TextView text;
        TextView date;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.commenter_name);
            header=(CircleImageView) itemView.findViewById(R.id.commenter_header);
            text = (TextView) itemView.findViewById(R.id.comment_talk);
            date = (TextView)itemView.findViewById(R.id.comment_publish_time);
        }
    }
}
