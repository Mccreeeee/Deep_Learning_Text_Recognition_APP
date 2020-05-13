package com.example.dell.myui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dell.myui.BaiduTranslate.Translate;
import com.example.dell.myui.BaiduTranslate.UnicodeDecode;
import com.example.dell.myui.R;
import com.example.dell.myui.utils.fastJsonUtils;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TranslateActivity extends AppCompatActivity {
private NiceSpinner original_language;
private NiceSpinner goal_language;
private Button btn_translate;
private EditText et_from;
private EditText et_to;
private RelativeLayout relative_back;
private String from;  //源语言
private String to; //目的语言
private String query;  //待翻译文本
private String transResult;//结果
List<String> ol=new LinkedList<>(Arrays.asList("auto","Chinese","Japan","English","France"));
List<String> gl=new LinkedList<>(Arrays.asList("Chinese","Japan","English","France"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        initView();
        original_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        from="auto";
                        Toast.makeText(TranslateActivity.this,"auto",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        from = "zh";
                        Toast.makeText(TranslateActivity.this,"Chinese",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        from = "jp";
                        Toast.makeText(TranslateActivity.this,"Japan",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        from = "en";
                        Toast.makeText(TranslateActivity.this,"English",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        from = "fra";
                        Toast.makeText(TranslateActivity.this,"French",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        goal_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        to = "zh";
                        Toast.makeText(TranslateActivity.this,"Chinese",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        to ="jp";
                        Toast.makeText(TranslateActivity.this,"Japan",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        to = "en";
                        Toast.makeText(TranslateActivity.this,"English",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        to = "fra";
                        Toast.makeText(TranslateActivity.this,"France",Toast.LENGTH_SHORT).show();
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_translate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                query = et_from.getText().toString();
                if(query.equals("")){
                    Log.d("translate","query==\"\"");
                }else{
                    // Android 4.0 之后不能在主线程中请求HTTP请求
                    Thread thread=new Thread(new Runnable(){
                        @Override
                        public void run() {
                                Translate translate = new Translate();
                                transResult = translate.transResult(query,from,to);

                        }
                    });
                       thread.start();
                       try{
                           thread.join();
                       } catch (InterruptedException e){
                           e.printStackTrace();
                       }
                       String decodeRes = UnicodeDecode.decodeUnicode(transResult);
                        et_to.setText(fastJsonUtils.decodeTransJson(decodeRes));
                }
            }
        });
    }
    void initView()
    {
        from ="auto";
        to = "zh";
        query = "";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        query = bundle.getString("query");
        original_language=(NiceSpinner)findViewById(R.id.nice_spinner1);
        goal_language=(NiceSpinner)findViewById(R.id.nice_spinner2);

        original_language.attachDataSource(ol);
        original_language.setBackgroundResource(R.drawable.textview_round_border);
        original_language.setTextColor(Color.WHITE);
        original_language.setTextSize(15);

        goal_language.attachDataSource(gl);
        goal_language.setBackgroundResource(R.drawable.textview_round_border);
        goal_language.setTextColor(Color.WHITE);
        goal_language.setTextSize(15);

        relative_back = (RelativeLayout)findViewById(R.id.translate_back);
        ImageView his_back = (ImageView)relative_back.findViewById(R.id.his_back);
        his_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        et_from = (EditText)findViewById(R.id.olanguage_text);
        et_to = (EditText)findViewById(R.id.glanguage_text);
        btn_translate = (Button) findViewById(R.id.btn_translate);
        et_from.setText(query);

    }
}
