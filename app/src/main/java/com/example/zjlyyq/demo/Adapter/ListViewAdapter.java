package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zjlyyq.demo.Home;
import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageImage;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by jialuzhang on 2017/3/28.
 */
public class ListViewAdapter extends BaseAdapter implements View.OnClickListener,AdapterView.OnItemClickListener{
    private Context mContext;
    private ArrayList<Message> messages;
    private ArrayList<User> users;
    ArrayList<ArrayList<String>> imageUrlss;
    FragmentManager fm;
    GalikGridViewAdapter adapter;
    private Callback mCallback;
    private int index = 0;
    int isclick;
    public ListViewAdapter(Context context,ArrayList<Message> messages,ArrayList<User> users,FragmentManager fm,ArrayList<ArrayList<String>> imageUrlss,Callback callback){
        this.mContext = context;
        this.messages = messages;
        this.users = users;
        this.fm = fm;
        this.mCallback = callback;
        this.imageUrlss = imageUrlss;
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        isclick = 0;
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.simple_item,null,false);
            holder.textView = (TextView) convertView.findViewById(R.id.name);
            holder.textView2=(TextView) convertView.findViewById(R.id.talk);
            holder.imageView=(ImageView)convertView.findViewById(R.id.header);
            holder.gridView = (GridView) convertView.findViewById(R.id.gridview);
            holder.publish_time = (TextView)convertView.findViewById(R.id.publish_time);
            holder.bt_comment = (ImageButton)convertView.findViewById(R.id.bt_comment);
            holder.bt_favor = (ImageButton)convertView.findViewById(R.id.bt_favor);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Message message = this.messages.get(position);
        User user = this.users.get(position);
        final int user_id = message.getPublisherId();
        //设置发布时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(message.getPublishDate());
        holder.publish_time.setText(sdf.format(date));
        //设置头像
        if (user.getUserPhoto().equals("null")){
            holder.imageView.setImageResource(R.drawable.touxiang);
        }
        else {
            Glide.with(this.mContext).load(user.getUserPhoto()).into(holder.imageView);
        }
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(),"fonts/yy.TTF");
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,position);
            }
        });

        //设置用户名
        holder.textView.setText(user.getUserName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,position);
            }
        });
        holder.textView.setTypeface(tf);
        //holder.textView.setOnClickListener(this);
        //设置内容主体
        holder.textView2.setText(message.getText());
        holder.textView2.setTypeface(tf);
        //设置图片
        GalikGridViewAdapter adapter = new GalikGridViewAdapter(this.mContext,imageUrlss.get(position));
        holder.gridView.setAdapter(adapter);
        holder.bt_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,position);
            }
        });
        holder.bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.click(v,position);
            }
        });
        return convertView;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    public interface Callback{
        public void click(View v,int postion);
    }
    @Override
    public void onClick(View v) {
        mCallback.click(v,0);
    }
    private class ViewHolder {
        TextView publish_time;
        ImageView imageView;
        TextView textView;
        TextView textView2;
        GridView gridView;
        ImageButton bt_favor;
        ImageButton bt_comment;
    }

}
