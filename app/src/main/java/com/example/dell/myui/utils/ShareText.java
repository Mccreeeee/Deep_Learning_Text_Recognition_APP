package com.example.dell.myui.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.dell.myui.R;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;

public class ShareText {
    //分享提取的文字
    public static void shareText(Context context, String text, String title){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);//Text为文本的内容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//为Activity新建一个任务栈
        context.startActivity(
                Intent.createChooser(intent, title));//R.string.action_share同样是标题

    }
    //先生成pdf文件
    public static void createPdf(String pdfPath ,String picturePath, String text){
        CreatePdfUtils createPdfUtils = null;
        try{
            createPdfUtils = (new CreatePdfUtils(pdfPath)).addTitleToPdf("test")
                    .addImageToPdfCenterH(picturePath,200,200)
                    .addTextToPdf(text);
        }catch (IOException e){
            e.printStackTrace();

        }catch (DocumentException e){
            e.printStackTrace();
        }finally {
            createPdfUtils.close();
        }
    }

    //分享多种格式的文件
    public static void shareFile(Context context, File file,String title, String fileType){
        Intent intent = new Intent(Intent.ACTION_SEND);
        if(fileType.equals("pdf")){
            intent.setType("application/pdf");
        }
        else {
            intent.setType("text/plain");
        }

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        context.startActivity(
                Intent.createChooser(intent, title));//R.string.action_share同样是标题
    }
}
