package com.example.zjlyyq.demo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zjlyyq.demo.Adapter.FansRecyclerAdapter;
import com.example.zjlyyq.demo.Adapter.FellowRecyclerAdapter;
import com.example.zjlyyq.demo.Tools.UserTools;
import com.example.zjlyyq.demo.models.User;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class FellowActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<User> fellows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fellow);
        fellows = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.fans_recycle);
        new GetEveryFellow().execute();
    }

    class GetEveryFellow extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            Iterator<Integer> iterator = MapFragment.myFellows.iterator();
            while (iterator.hasNext()){
                int id = iterator.next();
                User user = UserTools.getUserObjectById(id);
                fellows.add(user);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FellowRecyclerAdapter adapter = new FellowRecyclerAdapter(FellowActivity.this,fellows);
            recyclerView.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(FellowActivity.this);
            recyclerView.setLayoutManager(manager);
        }
    }
}
