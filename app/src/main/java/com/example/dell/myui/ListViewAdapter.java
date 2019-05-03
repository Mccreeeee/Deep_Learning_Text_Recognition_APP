package com.example.dell.myui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    List<String>menu_list;

    public ListViewAdapter(Context context,List<String>list) {
        super();
        this.context = context;
        menu_list=list;
    }
    @Override
    public int getCount()
    {
        if(menu_list.size()!=0)
            return menu_list.size();
        return 0;
    }
    @Override
    public String getItem(int position) {
        if (menu_list != null) {
            return menu_list.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                  TextView str;
                  if(convertView==null)
                  {

                    str=new TextView(context);
                      convertView=LayoutInflater.from(context).inflate(R.layout.listview_item,null);
                      convertView.setTag(str);
                  }
                  else
                  {str=(TextView)convertView.getTag();
                  str=(TextView)convertView.findViewById(R.id.item_textview);
                  str.setText(menu_list.get(position));}
                  return convertView;
}
}
