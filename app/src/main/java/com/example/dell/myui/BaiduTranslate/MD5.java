package com.example.dell.myui.BaiduTranslate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    /*MD5编码相关的类
    * */
    private static char[] hexDDigts = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    public static String md5(String input){
        if(input == null)
            return  null;

        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = input.getBytes("utf-8");
            messageDigest.update(inputByteArray);
            byte[] resultByte = messageDigest.digest();

            return byteArrayToHex(resultByte);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArrayToHex(byte[] bytes){
        char[] resultCharArray = new char[bytes.length * 2];

        int index = 0;
        for(byte b:bytes){
            resultCharArray[index++] = hexDDigts [b>>> 4 & 0xf];
            resultCharArray[index++] = hexDDigts[b & 0xf];
        }
        return new String(resultCharArray);
    }

}
