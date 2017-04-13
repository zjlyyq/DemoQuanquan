package com.example.zjlyyq.demo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageImage;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/28.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Message> messages;
    private ArrayList<User> users;
    FragmentManager fm;
    public ListViewAdapter(Context context,ArrayList<Message> messages,ArrayList<User> users,FragmentManager fm){
        this.mContext = context;
        this.messages = messages;
        this.users = users;
        this.fm = fm;
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
        final int user_id = message.getPublisherId();
        holder.imageView.setImageResource(R.drawable.touxiang);
        holder.textView.setText(users.get(position).getUserName());
        holder.textView2.setText(message.getText());
        new Thread(){
            @Override
            public void run() {
                try {
                    User user = getUser(user_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

        int message_id = message.getMessageId();
        MessageImage messageImage = new MessageImage(mContext,1,1);
        ArrayList<Bitmap> pictures = messageImage.getPicturesByMessageId(message_id);
        GridViewAdapter adapter = new GridViewAdapter(mContext,pictures,fm);
        holder.gridView.setAdapter(adapter);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView2;
        GridView gridView;
    }
    public User getUser(int userid) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",userid);
        HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
        String result = httpTool.jsonResult();
        JSONObject user = new JSONObject(result);
        User user1 = new User(jsonObject);
        Log.d("TEST",user.toString());
        return user1;
    }
    private class MyAsyncTask extends AsyncTask<Integer, Void, User> {
        @Override
        protected User doInBackground(Integer... integers) {
            for (Integer integer : integers){
                try {
                    return getUser(integer);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
        }
    }
}
