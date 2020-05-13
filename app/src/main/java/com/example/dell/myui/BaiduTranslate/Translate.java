package com.example.dell.myui.BaiduTranslate;

import java.util.HashMap;
import java.util.Map;

public class Translate {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid = "20200501000436176";
    private String securityKey = "cgNthhn3a2DWIrhLNncx";

    public String transResult(String query,String from, String to){
        Map<String, String>params = buildParams(query, from, to);
        return HttpServer.get(TRANS_API_HOST, params);

    }
    //query、是待翻译的文本，from是源语言，to是目的语言
    public Map<String, String> buildParams(String query,String from, String to){
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to",to);

        params.put("appid", appid);

        //随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt",salt);

        //签名
        String src = appid + query + salt + securityKey;
        params.put("sign",MD5.md5(src));

        return params;  //要发送http请求的参数
    }
}
