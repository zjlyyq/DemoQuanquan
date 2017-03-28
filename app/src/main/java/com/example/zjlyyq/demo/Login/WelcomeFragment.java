package com.example.zjlyyq.demo.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zjlyyq.demo.MainActivity;
import com.example.zjlyyq.demo.R;

/**
 * Created by jialuzhang on 2017/3/19.
 */

public class WelcomeFragment extends Fragment {
    private static final String SharedPreferencesFileName = "userInfo";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static int user_id;
    private Button register_bt;
    private Button login_bt;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("WelcomeFragment","start");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_fragment,container,false);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);   //从userInfo文件中读取是否由用户已登陆信息
        user_id = sharedPreferences.getInt(SharedPreferencesFileName,-1);
        /*若发现用户已经登陆过，直接跳转即可
        if(user_id != -1){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        */
        register_bt = (Button)view.findViewById(R.id.register_bt);
        login_bt = (Button)view.findViewById(R.id.to_login_bt);
        //Log.d("WelcomeFragment","buju");
        register_bt.setOnClickListener(new ButtonOnClickListener());
        login_bt.setOnClickListener(new ButtonOnClickListener());
        return view;
    }

    class ButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Log.d("WelcomeFragment","注册按钮被按下");
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction  transaction = fm.beginTransaction();
            switch (view.getId()){
                case R.id.register_bt:
                    Log.d("WelcomeFragment","注册按钮被按下");
                    Log.d("TEST","开始注册");
                    Fragment registerFragment = new RegisterFragment();
                    transaction.replace(R.id.fragment_container,registerFragment).commit();
                    break;
                case R.id.to_login_bt:
                    Fragment loginFragment = new LoginFragment();
                    transaction.replace(R.id.fragment_container,loginFragment).commit();
                    break;
                default:
                    return;
            }
        }
    }
}
