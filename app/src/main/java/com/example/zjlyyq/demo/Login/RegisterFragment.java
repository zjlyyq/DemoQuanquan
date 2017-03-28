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
import android.widget.EditText;
import android.widget.Toast;

import com.example.zjlyyq.demo.MainActivity;
import com.example.zjlyyq.demo.MapFragment;
import com.example.zjlyyq.demo.R;
import com.example.zjlyyq.demo.models.User;

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment,container,false);
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferencesFileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        email_et = (EditText)view.findViewById(R.id.email_et);
        username_et = (EditText)view.findViewById(R.id.username_et);
        passwd_et = (EditText)view.findViewById(R.id.passwd_et);
        repter_et = (EditText)view.findViewById(R.id.repter_passwd);
        commit_bt = (Button) view.findViewById(R.id.commt_bt);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction transaction = fm.beginTransaction();
        commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(getActivity());
                user.setUserName(username_et.getText().toString());
                user.setEmail_adress(email_et.getText().toString());
                user.setPassword(passwd_et.getText().toString());
                //Log.d("TEST","就看看");
                //Toast.makeText(getActivity(),user.getEmail_adress(),Toast.LENGTH_SHORT).show();
                user_id = user.insertIntoDatebase();
                if(user_id != -1){
                    editor.putInt("userId",user_id);
                    Toast.makeText(getActivity(),TAG+":记录插入并返回成功",Toast.LENGTH_SHORT).show();
                    Log.d("TEST","user_id = "+user_id);
                    MapFragment mapFragment = MapFragment.newInstance(user_id);
                    transaction.replace(R.id.fragment_container,mapFragment).commit();
                }
            }
        });
        return view;
    }
}
