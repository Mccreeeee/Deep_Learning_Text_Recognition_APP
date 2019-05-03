package com.example.dell.myui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

public class FragmentText extends Fragment {
    private BoomMenuButton boomMenuButton;
    private static String[]str=new String[]{"翻译","搜索","导出","复制"};
    private static int index=0;
    private static int imageIndex=0;
    private static int[]imageResours=new int[]{R.drawable.translate,R.drawable.search,R.drawable.export,
    R.drawable.copy};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_text, container, false);
        boomMenuButton=view.findViewById(R.id.bmb);
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
                         Intent intent=new Intent(getActivity(),TranslateActivity.class);
                         startActivity(intent);
                         break;
                     case 1:
                         Toast.makeText(getActivity(),"搜索",Toast.LENGTH_SHORT).show();
                         break;
                     case 2:
                         Toast.makeText(getActivity(),"导出",Toast.LENGTH_SHORT).show();
                         showButtonDialog();
                         break;
                     case 3:
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
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_pdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"pdf",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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
