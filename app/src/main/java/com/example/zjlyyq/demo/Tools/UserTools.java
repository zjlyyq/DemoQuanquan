package com.example.zjlyyq.demo.Tools;

import com.example.zjlyyq.demo.HttpTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
}
