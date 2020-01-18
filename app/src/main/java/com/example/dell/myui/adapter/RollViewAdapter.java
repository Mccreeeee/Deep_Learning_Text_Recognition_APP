package com.example.dell.myui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dell.myui.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
//实现无x
public class RollViewAdapter extends LoopPagerAdapter {

    private int[]pictures={R.drawable.picture_show, R.drawable.picture_show,R.drawable.picture_show};
    public RollViewAdapter(RollPagerView rollPagerView)
    {
       super(rollPagerView);
    }
    @Override
    public View getView(ViewGroup container,int position)
    {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(pictures[position]);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
    public int getRealCount()
    {
        return pictures.length;
    }

}
