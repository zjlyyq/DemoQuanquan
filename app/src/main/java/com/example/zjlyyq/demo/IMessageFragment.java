package com.example.zjlyyq.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zjlyyq.demo.Adapter.IMessageAdapter;
import com.example.zjlyyq.demo.Adapter.ListViewAdapter;
import com.example.zjlyyq.demo.Emotion.EmotionEditView;
import com.example.zjlyyq.demo.Emotion.EmotionTextView;
import com.example.zjlyyq.demo.models.IMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jialuzhang on 2017/4/17.
 */

public class IMessageFragment extends Fragment {
    Toolbar mToolBar;
    TextView talk_to_who;
    Button sendbt;
    ListView listview;
    int user_id;
    IMessageAdapter iMessageAdapter;
    int my_id;
    private ClientThread clientThread;
    private ArrayList<IMessage> imessages;
    EmotionTextView emotionTextView;
    public static IMessageFragment newInstance(int userid,int myid){
        IMessageFragment iMessageFragment = new IMessageFragment();
        Bundle argc = new Bundle();
        argc.putInt("userId",userid);
        argc.putInt("myId",myid);
        iMessageFragment.setArguments(argc);
        return  iMessageFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_id = getArguments().getInt("userId");
        my_id = getArguments().getInt("myId");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.talk,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(View v){
        imessages = new ArrayList<IMessage>();
        mToolBar = (Toolbar)v.findViewById(R.id.talk_toolbar);
        talk_to_who = (TextView)v.findViewById(R.id.talk_with_who);
        listview = (ListView)v.findViewById(R.id.talklist);
        mToolBar.setNavigationIcon(R.mipmap.back);
        sendbt = (Button)v.findViewById(R.id.talk_sengBt);
        emotionTextView = (EmotionTextView)v.findViewById(R.id.talk_imessage);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0x123:
                        Toast.makeText(getActivity(),"可以发送",Toast.LENGTH_LONG).show();
                        sendbt.setClickable(true);
                        sendbt.setText("发送");
                        break;
                    case 0x234:
                        try {
                            JSONObject json = new JSONObject(msg.obj.toString());
                            Log.d("SOCKETrec",json.toString());
                            IMessage im = new IMessage(json);
                            imessages.add(im);
                            iMessageAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        iMessageAdapter = new IMessageAdapter(imessages,getActivity(),my_id);
        listview.setAdapter(iMessageAdapter);
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start();
        sendbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMessage iMessage = new IMessage();
                Log.d("SOCKET","imessage start");
                if (user_id > my_id){
                    iMessage.setUser1(my_id);
                    iMessage.setUser2(user_id);
                    iMessage.setDirection(1);
                }else {
                    iMessage.setUser2(my_id);
                    iMessage.setUser1(user_id);
                    iMessage.setDirection(-1);
                }
                iMessage.setTypeImessage(0);
                iMessage.setText(emotionTextView.getText().toString());
                Map<String,Object> mapObject = new HashMap<String, Object>();
                mapObject.put("date",iMessage.getDate());
                mapObject.put("user1",iMessage.getUser1());
                mapObject.put("user2",iMessage.getUser2());
                mapObject.put("text",iMessage.getText());
                mapObject.put("typeImessage",iMessage.getTypeImessage());
                mapObject.put("direction",iMessage.getDirection());
                JSONObject jsonObject = new JSONObject(mapObject);
                Message msg = new Message();
                msg.what = 0x345;
                msg.obj = jsonObject.toString();
                Log.d("SOCKET","json= "+jsonObject.toString());
                clientThread.revHandler.sendMessage(msg);
                //Log.d("SOCKET","json succeed= "+jsonObject.toString());
                emotionTextView.setText("");
            }
        });
        new Thread(){
            @Override
            public void run() {
                Log.d("MYTHEAD","0");
                while (true){
                    if (emotionTextView.getText().toString().length() > 0){
                        Message msg = new Message();
                        Log.d("MYTHEAD","ok");
                        msg.what = 0x123;
                        handler.sendMessage(msg);
                        break;
                    }
                }
            }
        }.start();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.mymenu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
