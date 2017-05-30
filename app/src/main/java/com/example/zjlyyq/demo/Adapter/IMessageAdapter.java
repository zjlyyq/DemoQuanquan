package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.IMessage;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/4/17.
 */

public class IMessageAdapter extends BaseAdapter {
    ArrayList<IMessage> iMessages;
    Context mContext;
    int yourId;
    //int hisId;

    public IMessageAdapter(ArrayList<IMessage> list,Context context,int id){
        this.iMessages = list;
        this.mContext = context;
        this.yourId = id;
    }
    @Override
    public int getCount() {
        return iMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return iMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        IMessage iMessage = iMessages.get(position);
        holder = new ViewHolder();
        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.imessage,null,false);
        if ((iMessage.getUser1() == yourId && iMessage.getDirection() == 1) ||
                    (iMessage.getUser2() == yourId && iMessage.getDirection() == -1) ){
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.imessageyou,null,false);
                holder.textView = (TextView) convertView.findViewById(R.id.talksomethingyou);
            holder.timeShow = (TextView)convertView.findViewById(R.id.timelineyou);
        }else{
                convertView = LayoutInflater.from(this.mContext).inflate(R.layout.imessage,null,false);
                holder.textView = (TextView) convertView.findViewById(R.id.talksomething);
            holder.timeShow = (TextView)convertView.findViewById(R.id.timeline);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long date = iMessage.getDate();
        holder.timeShow.setText(simpleDateFormat.format(date));
        holder.textView.setText(iMessage.getText());;
        return convertView;
    }

    private class ViewHolder {
        TextView textView;
        TextView timeShow;
    }
}
