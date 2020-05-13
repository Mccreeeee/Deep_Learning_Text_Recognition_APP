package com.example.dell.myui.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

public class fastJsonUtils {
    public static Map JsonToMap(String jsondata){
        if(TextUtils.isEmpty(jsondata)){
            return null;
        }
        Map map =null;
        try{
            map = parseObject(jsondata, new TypeReference<Map>(){});
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    public  static String MapToJson(Map<String,Object>map){
        if(map.isEmpty()){
            return null;
        }
        String jsondata = null;
        try{
            jsondata = JSON.toJSONString(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsondata;
    }

    public  static String objectToJson(Object object){

        if(object == null){
            return null;
        }
        String jsondata = null;
        try{
        jsondata = JSON.toJSONString(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsondata;
    }
    public static String parseJsonFromBaidu(String result,String result_name,String list_result_name){
        //将调用百度ocr或者翻译获得的json数据转化为字符串
        String strRes="";
        try {
            JSONObject jsonRes = new JSONObject(result);
            JSONArray jsonRes_list = new JSONArray(jsonRes.getString(result_name));
            int length = jsonRes_list.length();
            for(int i=0;i<length;i++){
                String string = jsonRes_list.getString(i);
                JSONObject jsonRes2 = new JSONObject(string);
                strRes+=jsonRes2.getString(list_result_name) + "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strRes;
    }
    public static String decodeJson(String result){
        //将调用百度ocr获得的json数据转化为字符串
        return parseJsonFromBaidu(result,"words_result","words");
    }
    public static String decodeTransJson(String result) {
       //百度翻译获得的json转化为结果
        return parseJsonFromBaidu(result,"trans_result","dst");
    }
}
