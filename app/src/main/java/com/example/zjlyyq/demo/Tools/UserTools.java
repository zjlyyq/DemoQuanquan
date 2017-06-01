package com.example.zjlyyq.demo.Tools;

import android.util.Log;

import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jialuzhang on 2017/5/11.
 */

public class UserTools {
    public static JSONObject getUserById(int id){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",id);
        JSONObject jsonObject = new JSONObject(map);
        HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
        try {
            String string = httpTool.jsonResult();
            return  new JSONObject(string);
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public static User getUserObjectById(int id){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",id);
        JSONObject jsonObject = new JSONObject(map);
        HttpTool httpTool = new HttpTool("GetUserById",jsonObject);
        try {
            String string = httpTool.jsonResult();
            return  new User(new  JSONObject(string) );
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public static HashSet<Integer> getFavorById(int id) throws IOException, JSONException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",id);
        JSONObject jsonObject = new JSONObject(map);
        HttpTool httpTool = new HttpTool("GetFavorsByUserId",jsonObject);

        String string = httpTool.jsonResult();
        JSONObject json = new JSONObject(string);
        int count = json.getInt("count");
        HashSet<Integer> favors = new HashSet<>();
        for(int i = 0;i < count;i ++){
            int tempId = json.getInt(""+i);
            favors.add(tempId);
        }
        return favors;
    }

    public static void updateFavor(int userid,int messageid,int p) throws IOException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userid);
        map.put("messageId",messageid);
        JSONObject jsonObject = new JSONObject(map);
        if (p == 1){
            HttpTool httpTool = new HttpTool("InsertIntoFavor",jsonObject);
            httpTool.jsonResult();
        }else {
            HttpTool httpTool = new HttpTool("DeleteFromFavor",jsonObject);
            httpTool.jsonResult();
        }
    }
    public static void updateFellow(int userid,int hisId,int p) throws IOException {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user1Id",userid);
        map.put("user2Id",hisId);
        JSONObject jsonObject = new JSONObject(map);
        if (p == 1){
            HttpTool httpTool = new HttpTool("InsertIntoRelation",jsonObject);
            httpTool.jsonResult();
        }else {
            HttpTool httpTool = new HttpTool("DeleteFromRelation",jsonObject);
            httpTool.jsonResult();
        }
    }
    public static Set<Integer> getFellows(int id) throws JSONException, IOException {
        HashSet<Integer> fansIds = new HashSet<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",id);
        HttpTool httpTool = new HttpTool("GetFellowsById",jsonObject);
        Log.d("Fellow","获取关注");
        String result = httpTool.jsonResult();
        Log.d("Fellow","result"
                +result);
        JSONObject json = new JSONObject(result);
        int count = json.getInt("count");
        for (int i = 0;i < count;i ++){
            int tmpId = json.getInt(""+i);
            fansIds.add(tmpId);
        }
        return fansIds;
    }
}
