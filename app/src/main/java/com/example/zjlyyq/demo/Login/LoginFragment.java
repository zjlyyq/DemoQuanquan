package com.example.zjlyyq.demo.Login;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.zjlyyq.demo.MapFragment;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;

/**
 * Created by jialuzhang on 2017/3/19.
 */

public class LoginFragment extends Fragment {
    private EditText login_username_et;
    private EditText login_passwd_et;
    private Button login_bt;
    private static int user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        Thread checkThread = new Thread(){
            @Override
            public void run() {
                super.run();
            }
        };
        checkThread.run();
        login_username_et = (EditText)view.findViewById(R.id.login_username_et);
        login_passwd_et = (EditText)view.findViewById(R.id.login_passwd_et);
        login_bt = (Button)view.findViewById(R.id.login_bt);
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(getActivity());
                user_id = user.checkExisit(login_username_et.getText().toString());
                if(user_id == -1){
                    Toast.makeText(getActivity(),"对不起，没有此用户信息",Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("TEST2","login succeed:" + user_id);
                    MapFragment mapFragment = MapFragment.newInstance(user_id);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragment_container,mapFragment).commit();
                }
            }
        });
        return view;
    }
}
