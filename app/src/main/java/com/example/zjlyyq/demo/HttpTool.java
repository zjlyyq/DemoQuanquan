package com.example.zjlyyq.demo;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zjl on 2017/4/13.
 */

public class HttpTool {
    private  String url;
    private  JSONObject jsonObject;

    public HttpTool(String url,JSONObject jsonObject){
         this.jsonObject = jsonObject;
         this.url = url;
    }

    public String jsonResult() throws IOException {
        BufferedReader in = null;
        // 定义BufferedReader输入流来读取URL的响应
        PrintWriter out = null;
        try {
            String u = "http://192.168.199.115:8080/Quanquan/" + url;
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            System.out.println("连接");
            Log.d("TEST", "连接成功");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            Log.d("TEST", "获取URLConnection对象对应的输出流");
            out = new PrintWriter(conn.getOutputStream());
            Log.d("TEST", "获取URLConnection对象对应的输出流success");
            // 发送请求参数
            out.print("json=" + jsonObject.toString());
            //out.print("email="+jsonObject.getString("email_adress"));
            // flush输出流的缓冲
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            out.close();
            if (in != null) {
                in.close();
            }
        }
        return null;
    }
}
