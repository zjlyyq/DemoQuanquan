package com.example.zjlyyq.demo.Tools;

import com.example.zjlyyq.demo.HttpTool;
import com.example.zjlyyq.demo.models.Comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by jialuzhang on 2017/5/11.
 */

public class MessageTools {

    public static String getMessageById(int id){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("messageId",id);
        JSONObject jsonObject = new JSONObject(map);
        HttpTool httpTool = new HttpTool("GetMessageById",jsonObject);
        try {
            return httpTool.jsonResult();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static ArrayList<String> getImagesById(int id){
        Map<String,Object> messageMap = new HashMap<String,Object>();
        messageMap.put("messageId",id);
        JSONObject jsonObject = new JSONObject(messageMap);
        HttpTool httpTool = new HttpTool("GetImagesByMessageId",jsonObject);
        String result = "";
        ArrayList<String> imageUrls = new ArrayList<String>();
        try {
            result = httpTool.jsonResult();
            JSONObject jsonObject1 = new JSONObject(result);
            int count = jsonObject1.getInt("count");
            for (int j = 0;j < count;j ++){
                String url = jsonObject1.getString("imageUrl"+j);
                imageUrls.add(url);
            }
            return imageUrls;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static ArrayList<Comment> getCommentsById(int id){
        Map<String,Object> messageMap = new HashMap<String,Object>();
        messageMap.put("messageId",id);
        JSONObject jsonObject = new JSONObject(messageMap);
        HttpTool httpTool = new HttpTool("GetCommentsById",jsonObject);
        String result = "";
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            result = httpTool.jsonResult();
            JSONObject jsonObject1 = new JSONObject(result);
            int count = jsonObject1.getInt("count");
            for (int j = 0;j < count;j ++){
                JSONObject commentJson = new JSONObject(jsonObject.getString("comment"+j));
                Comment comment = new Comment(commentJson);
                comments.add(comment);
            }
            return comments;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  comments;
    }
}
