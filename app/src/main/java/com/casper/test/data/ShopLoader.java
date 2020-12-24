package com.casper.test.data;

import android.os.Handler;
import android.util.Log;

import com.casper.test.data.model.Shop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShopLoader {
    public ArrayList<Shop> getShops() {
        return shops;
    }

    private ArrayList<Shop> shops=new ArrayList<>();

    public String download(String urlString){
        try{
            //设置连接属性
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);//设置超时
            conn.setUseCaches(false);

            //连接
            conn.connect();
            //从这里开始真正获取数据
            InputStream inputStream = conn.getInputStream();
            InputStreamReader input = new InputStreamReader(inputStream);
            BufferedReader buffer = new BufferedReader(input);
            if(conn.getResponseCode()==200) {//200意味着返回的是“OK”,即成功返回网页
                String inputLine;
                StringBuffer resultData = new StringBuffer();
                //读取全部数据
                while ((inputLine=buffer.readLine())!=null){
                    resultData.append(inputLine);
                }
                String text = resultData.toString();
                Log.v("out------------------>",text);
                //返回数据
                return(text);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果出错，默认返回空字符串
        return "   ";
    }
    public void parseJson(String text){
        try {
            JSONObject jsonObject = new JSONObject(text);
            JSONArray jsonDatas = jsonObject.getJSONArray("shops");
            int length = jsonDatas.length();
            String test;
            for (int i=0;i<length;i++){
                JSONObject shopJson = jsonDatas.getJSONObject(i);
                Shop shop = new Shop();
                //注意数据格式的转换
                shop.setName(shopJson.getString("name"));
                shop.setLatitude(shopJson.getDouble("latitude"));
                shop.setLongitude(shopJson.getDouble("longitude"));
                shop.setMemo(shopJson.getString("memo"));
                shops.add(shop);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void load(final Handler handler, final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = download(url);
                parseJson(content);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

}
