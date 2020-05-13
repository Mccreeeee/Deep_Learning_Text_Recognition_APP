package com.example.dell.myui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myui.R;
import com.example.dell.myui.activity.TranslateActivity;
import com.example.dell.myui.utils.CreatTxtUtils;
import com.example.dell.myui.utils.FileUtils;
import com.example.dell.myui.utils.ShareText;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.File;

public class FragmentText extends Fragment {
    private BoomMenuButton boomMenuButton;
    private EditText ed_text_result;
    private ProgressBar pb_load;
    private TextView tv_load;
    private static String[]str=new String[]{"翻译","搜索","导出","复制"};
    private static int index=0;
    private static int imageIndex=0;
    private static int[]imageResours=new int[]{R.drawable.translate,R.drawable.search,R.drawable.export,
    R.drawable.copy};
    private String text_result="";
    private String picturePath = "";
    public  FragmentText(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_text, container, false);
        boomMenuButton=view.findViewById(R.id.bmb);
        ed_text_result = view.findViewById(R.id.et_result_text);
        ed_text_result.setText(text_result);
        pb_load = view.findViewById(R.id.pb_loading);
        tv_load = view.findViewById(R.id.tv_loading);
        pb_load.setVisibility(View.INVISIBLE);
        tv_load.setVisibility(View.INVISIBLE);
        for(int i=0;i<boomMenuButton.getPiecePlaceEnum().pieceNumber();i++)
        {
            TextOutsideCircleButton.Builder builder=new TextOutsideCircleButton.Builder().
                    listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                 switch(index)
                 {
                     case 0:
                         Toast.makeText(getActivity(),"翻译",Toast.LENGTH_SHORT).show();
                         Intent intent=new Intent(getActivity(), TranslateActivity.class);
                         Bundle bundle = new Bundle();
                         bundle.putString("query", text_result);
                         intent.putExtras(bundle);
                         startActivity(intent);
                         break;
                     case 1:
                         String url = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=2&tn=88093251_33_hao_pg&wd="+
                                 text_result+ "&rsv_spt=1&oq=%25E5%2591%25A8%25E6%259D%25B0%25E4%25BC%25A6&rsv_pq=97fab89"+
                                 "30020f351&rsv_t=77344EPH29pHSyyysOi2h0ph6QxxgDoRrl2FibyfqQPigge3AM3dVIRUeM7xCAfwjPLIY68bMOfC&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_sug3=31&rsv_sug1=17&rsv_sug7=100&rsv_sug2=0&rsv_btype=t&inputT=7249&rsv_sug4=7250";
                         Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                         startActivity(browserIntent);
                         Toast.makeText(getActivity(),"搜索",Toast.LENGTH_SHORT).show();
                         break;
                     case 2:
                         //导出功能
                         showButtonDialog();
                         break;
                     case 3:
                         ShareText.shareText(getContext(),text_result, "复制到");
                         Toast.makeText(getActivity(),"复制",Toast.LENGTH_SHORT).show();
                         break;
                 }
                        }
                    }).normalImageRes(getImage()).normalText(getText());
            boomMenuButton.addBuilder(builder);
        }
        return view;
    }
   static String getText()
    {
        if (index >= str.length) index = 0;
        return str[index++];
    }
    static int getImage()
    {
        if(imageResours.length<=imageIndex)  imageIndex=0;
         return imageResours[imageIndex++];
    }
    public void setText_result (String result){
        text_result = result;
    }
    public void setPicturePath(String path){
        picturePath = path;
    }
    public void showButtonDialog()
    {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(getActivity(),R.style.DialogTheme);
        //2、设置布局
        View view=View.inflate(getActivity(),R.layout.button_dialog,null);
        dialog.setContentView(view);

        Window window=dialog.getWindow();
        //设置弹出的位置
        window.setGravity(Gravity.BOTTOM);
        //设置动画
        window.setWindowAnimations(R.style.dialog_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.findViewById(R.id.tv_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(getActivity(),"txt",Toast.LENGTH_SHORT).show();
                File txtFile = FileUtils.getTxtFile(getContext());
                Toast.makeText(getActivity(),txtFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                CreatTxtUtils.createTxt(txtFile.getAbsolutePath(),text_result);
                ShareText.shareFile(getContext(),txtFile,"分享到","txt");
            }
        });
        dialog.findViewById(R.id.tv_pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"生成pdf需要一些时间，请耐心等候！",Toast.LENGTH_SHORT).show();
                File pdf = FileUtils.getPdfFile(getContext());
                if(pdf == null){
                    Toast.makeText(getActivity(),"不好意思，生成失败，请再尝试！",Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    pb_load.setVisibility(View.VISIBLE);
                    tv_load.setVisibility(View.VISIBLE);
                    /*pb_load.setVisibility(View.VISIBLE);
                    tv_load.setVisibility(View.VISIBLE);
                    ShareText.createPdf(pdf.getAbsolutePath(),picturePath,text_result);
                    ShareText.shareFile(getContext(),pdf,"分享到","pdf");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb_load.setVisibility(View.INVISIBLE);
                            tv_load.setVisibility(View.INVISIBLE);
                        }
                    },4000);*/
                    Handler msgHandle = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            pb_load.setVisibility(View.INVISIBLE);
                            tv_load.setVisibility(View.INVISIBLE);
                            ShareText.shareFile(getContext(),pdf,"分享到","pdf");
                            super.handleMessage(msg);
                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ShareText.createPdf(pdf.getAbsolutePath(),picturePath,text_result);
                            Message g = Message.obtain(msgHandle);
                            msgHandle.sendMessage(g);
                        }
                    }).start();
                    }

            }
        });
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
