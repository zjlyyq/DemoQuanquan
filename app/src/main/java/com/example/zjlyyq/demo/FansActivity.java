package com.example.zjlyyq.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.zjlyyq.demo.Adapter.FansRecyclerAdapter;

public class FansActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_layout);
        recyclerView = (RecyclerView)findViewById(R.id.fans_recycle);

        FansRecyclerAdapter adapter = new FansRecyclerAdapter(FansActivity.this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }
}
