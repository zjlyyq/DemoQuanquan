package com.example.zjlyyq.demo;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zjlyyq on 2016/11/7.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ArrayList<HashMap<String, Object>>> mList;
    private Context mContext;

    public ListViewAdapter(ArrayList<ArrayList<HashMap<String, Object>>> mList, Context mContext) {
        super();
        this.mList = mList;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        if (mList == null) {
            return 20;
        } else {
            return this.mList.size();
        }
    }
    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            return this.mList.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from
                    (this.mContext).inflate(R.layout.simple_item, null, false);
            holder.textView = (TextView) convertView.findViewById(R.id.name);
            holder.textView2=(TextView) convertView.findViewById(R.id.talk);
            holder.imageView=(ImageView)convertView.findViewById(R.id.header);
            holder.gridView = (GridView) convertView.findViewById(R.id.gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (this.mList != null) {
            String str9 = "总有个人会爱你不大的眼睛 不高的鼻梁 不完美的身材 不长的小短腿 不会下降的体重和你不要脸的性格";
            if (holder.textView != null) {
                Object username = this.mList.get(position).get(1).get("username");
                holder.textView.setText(username.toString());
            }
            if (holder.textView2 != null) {
                Object talk = this.mList.get(position).get(2).get("talk");
                holder.textView2.setText(talk.toString());
            }
            if(holder.imageView != null){
                //Object header = this.mList.get(position).get(0).get("hander");
                //Toast.makeText(AppCompatActivity,header.getClass().getName(),Toast.LENGTH_LONG).show();
                //holder.imageView.setImageResource(header.toString());
                Object header = this.mList.get(position).get(0).get("hander");
                //int image = (int)header;
                //holder.imageView.setImageResource(image);
                holder.imageView.setImageResource(R.drawable.touxiang7);
            }
            if (holder.gridView != null) {
                ArrayList<HashMap<String, Object>> arrayListForEveryGridView = this.mList.get(position);
                GridViewAdapter gridViewAdapter=new GridViewAdapter(mContext, arrayListForEveryGridView);
                holder.gridView.setAdapter(gridViewAdapter);
            }
        }
        return convertView;
    }


    private class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView2;
        GridView gridView;
    }
}
