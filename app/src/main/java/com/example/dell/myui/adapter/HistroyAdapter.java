package com.example.dell.myui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.myui.R;
import com.example.dell.myui.entity.HistoryItemEntity;

import java.util.List;

public class HistroyAdapter extends BaseAdapter {
private LayoutInflater inflater;
private List<HistoryItemEntity> list;
private OnShowItemClickListener onShowItemClickListener;
public HistroyAdapter(List<HistoryItemEntity> list, Context context)
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
    holder.name=(TextView)convertView.findViewById(R.id.history_date);
    holder.cb=(CheckBox)convertView.findViewById(R.id.history_checkbox);
    holder.data=(TextView)convertView.findViewById(R.id.history_time) ;
convertView.setTag(holder);
}
else
{
    holder=(ViewHolder) convertView.getTag();
}
final HistoryItemEntity item=list.get(position);
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
    holder.name.setText(item.getName());
    holder.data.setText(item.getData());
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
       TextView name;
       TextView data;
       CheckBox cb;
    }
    public interface OnShowItemClickListener
    {
        public void onShowItemClick(HistoryItemEntity hisItem);
        void onBackPressed();
    }
    public void setOnShowItemClickListener(OnShowItemClickListener onShowItemClickListener) {
        this.onShowItemClickListener = onShowItemClickListener;
    }
}
