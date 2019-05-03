package com.example.dell.myui.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myui.HistoryActivity;
import com.example.dell.myui.R;
import com.example.dell.myui.historyItem;
import com.example.dell.myui.histroyAdapter;

import java.util.ArrayList;
import java.util.List;

import hlq.com.slidedeletelistview.BtnDeleteListern;
import hlq.com.slidedeletelistview.SlideDeleteListView;

public class FragmentHistory extends Fragment implements histroyAdapter.OnShowItemClickListener{

    private SlideDeleteListView listView;
    private List<historyItem> dataList;
    private List<historyItem> selectList;
    private histroyAdapter hisAdapter;
    private RelativeLayout rootView;
    private LinearLayout opreateView;
    private static boolean isShow; // 是否显示CheckBox标识
    private View view;
    private LinearLayout lay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        this.view=view;
        init();
        return view;
    }
    public void init()
    {
        listView = view.findViewById(R.id.history_listview);
        lay= view.findViewById(R.id.lay);
        setlist();
        hisAdapter = new histroyAdapter(dataList,getActivity());
        listView.setAdapter(hisAdapter);
        hisAdapter.setOnShowItemClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(isShow)
                { isShow=false;}
                else
                {
                    isShow=true;
                    for(historyItem item:dataList)
                    {
                        item.setShow(true);
                    }
                    hisAdapter.notifyDataSetChanged();
                    showOpervate();
                    listView.setLongClickable(false);
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isShow)
                {
                    historyItem item=dataList.get(position);
                    boolean isCheck=item.isChecked();
                    if(isCheck){
                        item.setChecked(false);
                    }
                    else{item.setChecked(true);}
                    hisAdapter.notifyDataSetChanged();
                }
                else
                {
                    //单击事件
                    Toast.makeText(getActivity(), dataList.get(position).getDate(), Toast.LENGTH_SHORT).show();
                }
            }
        });
   /*     listView.setBtnDelClickListener(new BtnDeleteListern() {
            @Override
            public void deleteOnCliclListern(int positon) {
                Toast.makeText(getActivity(), "删除的是第" + positon + "项", Toast.LENGTH_SHORT).show();
            }
        });*/
    }


    @Override
    public void onShowItemClick(historyItem hisItem) {
        if(hisItem.isChecked()&&!selectList.contains(hisItem))
        {
            selectList.add(hisItem);
        }
        else if(!hisItem.isChecked()&&selectList.contains(hisItem))
        {
            selectList.remove(hisItem);
        }
    }
    /*
     * 显示操作界面
     * */
    public void showOpervate()
    {
        lay.setVisibility(View.VISIBLE);
        Animation anim= AnimationUtils.loadAnimation(getActivity(),R.anim.opervate_in);
        lay.setAnimation(anim);

        // 返回、删除、全选和反选按钮初始化及点击监听
        TextView tvBack =view.findViewById(R.id.operate_back);
        TextView tvDelete = view.findViewById(R.id.operate_delete);
        TextView tvSelect = view.findViewById(R.id.operate_select);
        TextView tvInvertSelect = view.findViewById(R.id.invert_select);


        tvBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isShow)
                {
                    selectList.clear();
                    for(historyItem item:dataList)
                    {
                        item.setShow(false);
                        item.setChecked(false);
                    }
                    hisAdapter.notifyDataSetChanged();
                    isShow=false;
                    listView.setLongClickable(true);
                    dismissOpervate();
                }
            }
        });
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(historyItem item:dataList)
                {
                    if(!item.isChecked())
                    {
                        item.setChecked(true);
                        if(!selectList.contains(item))
                        {
                            selectList.add(item);
                        }
                    }
                }
                hisAdapter.notifyDataSetChanged();
            }
        });
        tvInvertSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(historyItem item:dataList)
                {
                    if(!item.isChecked())
                    {
                        item.setChecked(true);
                        if(!selectList.contains(item))
                        {selectList.add(item);}
                    }
                    else
                    {
                        item.setChecked(false);
                        if(selectList.contains(item)){selectList.remove(item);}
                    }
                }
                hisAdapter.notifyDataSetChanged();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList != null && selectList.size() > 0) {
                    dataList.removeAll(selectList);
                    hisAdapter.notifyDataSetChanged();
                    selectList.clear();
                } else {
                    Toast.makeText(getActivity(), "请选择条目", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    /*隐藏操作界面*/
    public void dismissOpervate()
    {
        Animation anim=AnimationUtils.loadAnimation(getActivity(),R.anim.opervate_out);
        lay.setVisibility(View.GONE);
        lay.setAnimation(anim);
    }
    @Override
    public void onResume()
    {super.onResume();}
    @Override
    public void onBackPressed()
    {
        if(isShow)
        {
            selectList.clear();
            for(historyItem item:dataList)
            {
                item.setChecked(false);
                item.setShow(false);
            }
            hisAdapter.notifyDataSetChanged();
            isShow=false;
            listView.setLongClickable(true);
            dismissOpervate();
        }
        else{getActivity().onBackPressed();}
    }
    public void setlist()     //初始化列表
    {
        dataList=new ArrayList<historyItem>();
        selectList=new ArrayList<historyItem>();
        for(int i=0;i<1;i++)
        {
            dataList.add(new historyItem(BitmapFactory.decodeResource(getResources(), R.drawable.picture_show),"2016-01-01","00:00:00", false, false));
        }
    }
}
