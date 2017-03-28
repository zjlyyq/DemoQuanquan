package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageImage;
import com.example.zjlyyq.demo.models.MessageUnion;
import com.example.zjlyyq.demo.models.User;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/28.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Message> messages;
    public ListViewAdapter(Context context,ArrayList<Message> messages){
        this.mContext = context;
        this.messages = messages;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.simple_item,null,false);
            holder.textView = (TextView) convertView.findViewById(R.id.name);
            holder.textView2=(TextView) convertView.findViewById(R.id.talk);
            holder.imageView=(ImageView)convertView.findViewById(R.id.header);
            holder.gridView = (GridView) convertView.findViewById(R.id.gridview);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Message message = this.messages.get(position);
        int user_id = message.getPublisherId();
        User user = User.getUserById(user_id);
        if (user.getUserPhoto() == null){
            holder.imageView.setImageResource(R.drawable.touxiang);
        }else {
            holder.imageView.setImageBitmap(user.getUserPhoto());
        }
        holder.textView.setText(user.getUserName());
        holder.textView2.setText(message.getText());
        int message_id = message.getMessageId();
        MessageImage messageImage = new MessageImage(mContext,1,1);
        ArrayList<Bitmap> pictures = messageImage.getPicturesByMessageId(message_id);
        GridViewAdapter adapter = new GridViewAdapter(mContext,pictures);
        holder.gridView.setAdapter(adapter);

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView2;
        GridView gridView;
    }

}
