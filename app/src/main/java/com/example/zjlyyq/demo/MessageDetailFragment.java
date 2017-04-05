package com.example.zjlyyq.demo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zjlyyq.demo.Adapter.*;
import com.example.zjlyyq.demo.Adapter.GridViewAdapter;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.MessageImage;
import com.example.zjlyyq.demo.models.User;

import java.util.ArrayList;

/**
 * Created by jialuzhang on 2017/3/29.
 */
public class MessageDetailFragment extends Fragment {
    private Message message;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = new Message(getActivity());
        int message_id = getActivity().getIntent().getIntExtra("MESSAGEID",-1);
        Log.d("TEST2","id = "+message_id);
        message = message.getMessageById(message_id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_detail_layout,container,false);
        ImageView userPhoto;
        TextView userName;
        TextView userTalk;
        GridView message_photos;
        userName = (TextView)view.findViewById(R.id.meaaage_name);
        userPhoto = (ImageView)view.findViewById(R.id.message_header);
        userTalk = (TextView)view.findViewById(R.id.meaaage_text);
        message_photos = (GridView)view.findViewById(R.id.meaaage_gridview);
        userTalk.setText(message.getText());
        int user_id = message.getPublisherId();
        User user = User.getUserById(user_id);
        if (user.getUserPhoto()!=null){
            userPhoto.setImageBitmap(user.getUserPhoto());
        }else {
            userPhoto.setImageResource(R.drawable.touxiang);
        }
        userName.setText(user.getUserName());
        ArrayList<Bitmap> pic = new ArrayList<Bitmap>();
        pic = MessageImage.getPicturesByMessageId(message.getMessageId());
        com.example.zjlyyq.demo.Adapter.GridViewAdapter adapter = new GridViewAdapter(getActivity(),pic,getActivity().getSupportFragmentManager());
        message_photos.setAdapter(adapter);
        Log.d("TEST2",message.getText());
        return view;
    }
}
