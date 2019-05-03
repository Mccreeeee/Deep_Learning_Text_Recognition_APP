package com.example.dell.myui;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class histroyAdapter extends BaseAdapter {
private LayoutInflater inflater;
private List<historyItem> list;
private OnShowItemClickListener onShowItemClickListener;
public histroyAdapter(List<historyItem> list, Context context)
{
    this.list=list;
    inflater=LayoutInflater.from(context);
}
@Override
    public int getCount()
{
    return list.size();
}
@Override
    public Object getItem(int position)
{
       return null;
}
@Override
    public long getItemId(int position)
{
    return 0;
}
@Override
    public View getView(int position, View convertView, ViewGroup parent)
{
ViewHolder holder;
if(convertView==null)
{
    holder=new ViewHolder();
    convertView=inflater.inflate(R.layout.history_item,null);
    holder.picture=(ImageView) convertView.findViewById(R.id.history_pic);
    holder.date=(TextView)convertView.findViewById(R.id.history_date);
    holder.cb=(CheckBox)convertView.findViewById(R.id.history_checkbox);
    holder.time=(TextView)convertView.findViewById(R.id.history_time) ;
convertView.setTag(holder);
}
else
{
    holder=(ViewHolder) convertView.getTag();
}
final historyItem item=list.get(position);
//是否是多选状态
    if(item.isShow())
    {
        holder.cb.setVisibility(View.VISIBLE);
    }
    else
    {
        holder.cb.setVisibility(View.GONE);
    }
    holder.picture.setImageBitmap(item.getPicture());
    holder.date.setText(item.getDate());
    holder.time.setText(item.getTime());
    holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                item.setChecked(true);
            }
            else
            {
                item.setChecked(false);
            }
            // 回调方法，将Item加入已选
            onShowItemClickListener.onShowItemClick(item);
        }
    });
    // 必须放在监听后面
    holder.cb.setChecked(item.isChecked());
    return convertView;
}
static class ViewHolder
    {
       ImageView picture;
       TextView date;
       TextView time;
       CheckBox cb;
    }
    public interface OnShowItemClickListener
    {
        public void onShowItemClick(historyItem hisItem);
        void onBackPressed();
    }
    public void setOnShowItemClickListener(OnShowItemClickListener onShowItemClickListener) {
        this.onShowItemClickListener = onShowItemClickListener;
    }
}
