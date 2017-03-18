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
            return 1;
        } else {
            return this.mList.size()+1;        //多返回一个，用于显示提示信息
        }
    }
    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        } else {
            if(position == this.mList.size()){
                return this.mList.get(position-1);
            }
            else{
                return this.mList.get(position);
            }
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(position == this.mList.size()){
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from
                        (this.mContext).inflate(R.layout.the_last, null, false);
                holder.textView = (TextView)convertView.findViewById(R.id.the_last);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return  convertView;
        }
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
            if (holder.textView != null) {
                Integer username = (Integer)this.mList.get(position).get(1).get("username");
                holder.textView.setText(username);
            }
            if (holder.textView2 != null) {
                Integer talk = (Integer)this.mList.get(position).get(2).get("talk");
                holder.textView2.setText(talk);
            }
            if(holder.imageView != null){
                Integer header = (Integer)this.mList.get(position).get(0).get("header");
                holder.imageView.setImageResource(header);
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
    //最后一行的显示信息只有一排文字
    private class ViewHolder2{
        TextView textView;
    }
}
