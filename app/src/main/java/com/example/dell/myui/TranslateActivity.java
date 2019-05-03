package com.example.dell.myui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TranslateActivity extends AppCompatActivity {
private NiceSpinner original_language;
private NiceSpinner goal_language;
List<String> ol=new LinkedList<>(Arrays.asList("Chinese","Japan","English","France"));
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
                        Toast.makeText(TranslateActivity.this,"Chinese",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(TranslateActivity.this,"Japan",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(TranslateActivity.this,"English",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(TranslateActivity.this,"France",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TranslateActivity.this,"Chinese",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(TranslateActivity.this,"Japan",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(TranslateActivity.this,"English",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(TranslateActivity.this,"France",Toast.LENGTH_SHORT).show();
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void initView()
    {
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

    }
}
