package com.example.zjlyyq.demo.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.MainActivity;
import com.example.zjlyyq.demo.MapFragment;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jialuzhang on 2017/3/19.
 */

public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private static final String SharedPreferencesFileName = "userInfo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static int user_id = -1;
    private EditText email_et;
    private EditText username_et;
    private EditText passwd_et;
    private EditText repter_et;
    private Button commit_bt;
    private static String BASE_URL = "172.15.22.183";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesFileName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email_et = (EditText)view.findViewById(R.id.email_et);
        username_et = (EditText)view.findViewById(R.id.username_et);
        passwd_et = (EditText)view.findViewById(R.id.passwd_et);
        repter_et = (EditText)view.findViewById(R.id.repter_passwd);
        repter_et = (EditText)view.findViewById(R.id.repter_passwd);
        commit_bt = (Button) view.findViewById(R.id.commt_bt);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123){
                    MapFragment mapFragment = MapFragment.newInstance(user_id);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.fragment_container,mapFragment).commit();
                }
            }
        };
        commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        //super.run();
                        registerCheck();
                        if (user_id == -2){
                            Toast.makeText(getActivity(),"此邮箱已经注册过，请直接登录",Toast.LENGTH_LONG).show();
                        }else if(user_id > 0){
                            editor.putInt("userId",user_id);
                            editor.commit();
                            Message msg = new Message();
                            msg.what = 0x123;
                            handler.sendMessage(msg);
                        }
                    }
                }.start();
            }

        });
        new Thread(){
            @Override
            public void run() {
                while (true){
                    if (email_et.getText().length() > 0 && username_et.getText().length() > 0
                            && passwd_et.getText().length() > 0 && repter_et.getText().length() > 0){
                        commit_bt.setAlpha(1);
                        commit_bt.setClickable(true);
                        break;
                    }
                }
            }
        }.start();

        return view;
    }

    public void registerCheck(){
        User user = new User(getActivity());
        user.setUserName(username_et.getText().toString());
        user.setEmail_adress(email_et.getText().toString());
        user.setPassword(passwd_et.getText().toString());
        Map<String,Object> usermap = new HashMap<String,Object>();
        usermap.put("userName",user.getUserName());
        usermap.put("password",user.getPassword());
        usermap.put("email_adress",user.getEmail_adress());
        JSONObject jsonObject = new JSONObject(usermap);
        System.out.println("json = "+jsonObject.toString());
        try {
            String u = HttpTool.BASE_URL + ":8080/Quanquan/RegisterCheck";
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();conn.setRequestProperty("accept", "*/*");
            System.out.println("连接");
            Log.d("TEST","连接成功");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            PrintWriter out = null;
            // 获取URLConnection对象对应的输出流
            Log.d("TEST","获取URLConnection对象对应的输出流");
            out = new PrintWriter(conn.getOutputStream());
            Log.d("TEST","获取URLConnection对象对应的输出流success");
            // 发送请求参数
            out.print("json="+jsonObject.toString());
            //out.print("email="+jsonObject.getString("email_adress"));
            // flush输出流的缓冲
            out.flush();
            BufferedReader in;
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
            Log.d("TEST",result);
            if (result.equals("此邮箱已经注册过，请直接登录")){
                user_id = -2;
                Log.d("TEST",""+user_id);
            }else {
                user_id = Integer.parseInt(result);
                Log.d("TEST",""+user_id);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
