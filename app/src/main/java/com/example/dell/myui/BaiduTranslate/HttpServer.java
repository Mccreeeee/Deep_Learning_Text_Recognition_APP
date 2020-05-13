package com.example.dell.myui.BaiduTranslate;

import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.InputMismatchException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


public class HttpServer {
    protected static final int SOCKET_TIMEOUT = 10000; //10s
    protected static final String GET = "GET";

    public static String get(String host, Map<String,String> params){
        try{
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{myX509TrustManager},null);
            String sendUrl = getUrlWithQueryString(host,params);

            URL uri = new URL(sendUrl);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();

            if (connection instanceof HttpsURLConnection){
                ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            connection.setConnectTimeout(SOCKET_TIMEOUT);
            connection.setRequestMethod(GET);
            int statusCode = connection.getResponseCode();
            if(statusCode != HttpURLConnection.HTTP_OK){
                System.out.println("HTTP 错误码：" + statusCode);
            }

            //读取服务器数据
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = br.readLine())!=null){
                builder.append(line);
            }
            String text = builder.toString();

            close(br);
            close(is);
            connection.disconnect();

            return text;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (KeyManagementException e){
            e.printStackTrace();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getUrlWithQueryString(String url, Map<String,String> params){
        if (params == null){return url;}

        StringBuilder builder = new StringBuilder(url);
        if(url.contains("?")){
            builder.append("&");
        } else {builder.append("?");}

        int i = 0;
        for(String key : params.keySet()){
            String value = params.get(key);
            if(value == null){
                continue;
            }
            if(i != 0){
                builder.append('&');
            }
            builder.append(key);
            builder.append("=");
            builder.append(encode(value));
            i++;
        }
        return builder.toString();
    }


    /*
    对输入的字符串进行url编码
     */
    protected static String encode(String input){
        if(input == null){return "";}
        try {
            return URLEncoder.encode(input, "utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return input;
    }

    protected static void close(Closeable closeable){
        if(closeable != null){
            try{
                closeable.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };
}
