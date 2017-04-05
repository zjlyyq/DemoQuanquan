package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.w3c.dom.Comment;
import com.example.zjlyyq.demo.models.*;
import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/29.
 */

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment>  comments){
        this.mContext = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    class ViewHolder{

    }
}
