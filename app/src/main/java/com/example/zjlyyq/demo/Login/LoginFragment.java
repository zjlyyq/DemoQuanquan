package com.example.zjlyyq.demo.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.MapFragment;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jialuzhang on 2017/3/19.
 */

public class LoginFragment extends Fragment {
    private EditText login_username_et;
    private EditText login_passwd_et;
    private Button login_bt;
    private static int user_id = -1;
    static int loginBt = -2;
    private static final String SharedPreferencesFileName = "userInfo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBt = -2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        login_username_et = (EditText)view.findViewById(R.id.login_username_et);
        login_passwd_et = (EditText)view.findViewById(R.id.login_passwd_et);
        login_bt = (Button)view.findViewById(R.id.login_bt);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123){
                    sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesFileName, MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putInt("userId",user_id);
                    editor.commit();
                    MapFragment mapFragment = MapFragment.newInstance(user_id);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragment_container,mapFragment).commit();
                }
            }
        };
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            loginCheck();
                            if(user_id == -2){
                                Toast.makeText(getActivity(),"用户名或密码不正确",Toast.LENGTH_LONG).show();
                            }
                            else if(user_id > 0){
                                Message msg = new Message();
                                msg.what = 0x123;
                                handler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        login_username_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loginBt ++;
                if (loginBt >= 0){
                    login_bt.setClickable(true);
                }
               return false;
            }
        });
        login_passwd_et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loginBt ++;
                if (loginBt >= 0){
                    login_bt.setAlpha(1);
                    login_bt.setClickable(true);
                }
                return false;
            }
        });
        return view;
    }
    public void loginCheck() throws IOException {
        User user = new User(getActivity());
        user.setUserName(login_username_et.getText().toString());
        user.setPassword(login_passwd_et.getText().toString());
        Map<String,Object> usermap = new HashMap<String,Object>();
        usermap.put("userName",user.getUserName());
        usermap.put("password",user.getPassword());
        //usermap.put("email_adress",user.getEmail_adress());
        JSONObject jsonObject = new JSONObject(usermap);
        Log.d("TEST",jsonObject.toString());
        //System.out.println("json = "+jsonObject.toString());
        BufferedReader in = null;
        // 定义BufferedReader输入流来读取URL的响应
        PrintWriter out = null;
        // 获取URLConnection对象对应的输出流
        try {
            String serverUrl = HttpTool.BASE_URL + ":8080/Quanquan/LoginCheck";
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();conn.setRequestProperty("accept", "*/*");
            System.out.println("连接");
            Log.d("TEST","连接成功");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.d("TEST","获取URLConnection对象对应的输出流");
            out = new PrintWriter(conn.getOutputStream());
            Log.d("TEST","获取URLConnection对象对应的输出流success");
            // 发送请求参数
            out.print("json="+jsonObject.toString());
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
            Log.d("TEST",result);
            if (result.equals("用户名或密码不正确")){
                user_id = -2;
            }else {
                user_id = Integer.parseInt(result);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null){
                out.close();
            }
            if (in != null){
                in.close();
            }
        }
    }
}
