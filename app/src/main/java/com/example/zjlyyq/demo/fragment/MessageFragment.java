package com.example.zjlyyq.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.example.zjlyyq.demo.Adapter.ListViewAdapter;
import com.example.zjlyyq.demo.Adapter.RecyclerAdapter;
import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.Message;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by jialuzhang on 2017/4/15.
 */

public class MessageFragment extends Fragment {
    RecyclerView recyclerView;
    private ArrayList<Message> messageList;
    private ArrayList<User> userList;
    ArrayList<ArrayList<String>> imageUrlss;
    ArrayList<String> imageUrls;
    private Context mContext;
    FragmentManager fm;
    int user_id;
    public MessageFragment(Context context,int id,FragmentManager fm){
        this.mContext = context;
        this.user_id = id;
        this.fm = fm;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.messagelist,container,false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recycleview);
        Log.d("TEST","开始构造listview");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        new MyAsyncTask().execute();
    }

    public  void getMessages(){
        Map<String,Object> userMap = new HashMap<String,Object>();
        userMap.put("userId",user_id);
        JSONObject jsonObject = new JSONObject(userMap);
        messageList = new ArrayList<Message>();
        Log.d("TEST","开始查询");
        Log.d("TEST",jsonObject.toString());
        HttpTool httpTool = new HttpTool("GetMessagesByUserId",jsonObject);
        String result = null;
        try {
            result = httpTool.jsonResult();
            if (result.equals("服务器错误")){
                Log.d("TEST","服务器错误");
                return;
            }
            JSONObject messageUnion = new JSONObject(result);
            int count = messageUnion.getInt("count");
            for (int i = 0;i < count;i ++){
                JSONObject jsonObject1 = messageUnion.getJSONObject("message"+i);
                Message message1 = null;
                try {
                    message1 = new Message(jsonObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("USERMESSAGE",""+message1.getText());
                messageList.add(message1);
                Log.d("MESSAGE",jsonObject1.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getUsers() throws JSONException, IOException {
        userList = new ArrayList<User>();
        Log.d("USERMESSAGE",""+messageList.size());
        for (int i = 0;i < messageList.size();i ++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",user_id);
            HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
            String result = httpTool.jsonResult();
            JSONObject user = new JSONObject(result);
            Log.d("USER",user.toString());
            User user1 = new User(user);
            Log.d("TESTUSER",""+user1.getUserId());
            userList.add(user1);
        }
    }
    private class MyAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                initDate();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new MyAsynysGetimages().execute(0);
        }
    }
    public void initDate() throws IOException, JSONException {
        getMessages();
        getUsers();
    }
    class MyAsynysGetimages extends AsyncTask<Integer,Void,Void> {
        String result = "";
        @Override
        protected Void doInBackground(Integer... params) {
            imageUrlss = null;
            imageUrlss = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < messageList.size(); i++) {
                Map<String, Object> messageMap = new HashMap<String, Object>();
                messageMap.put("messageId", messageList.get(i).getMessageId());
                JSONObject jsonObject = new JSONObject(messageMap);
                HttpTool httpTool = new HttpTool("GetImagesByMessageId", jsonObject);
                try {
                    result = httpTool.jsonResult();
                    imageUrls = null;
                    imageUrls = new ArrayList<String>();
                    JSONObject jsonObject1 = new JSONObject(result);
                    int count = jsonObject1.getInt("count");
                    for (int j = 0; j < count; j++) {
                        String url = jsonObject1.getString("imageUrl" + j);
                        imageUrls.add(url);
                    }
                    imageUrlss.add(imageUrls);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecyclerAdapter adapter = new RecyclerAdapter(getActivity(),messageList,userList,imageUrlss);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(manager);
        }
    }
}
