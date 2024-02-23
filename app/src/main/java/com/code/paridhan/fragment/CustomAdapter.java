package com.code.paridhan.fragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.code.paridhan.R;

public class CustomAdapter extends PagerAdapter{
    private Activity activity;
    private Integer[] imagesArray;
    private String[] namesArray;
    public CustomAdapter(Activity activity,Integer[] imagesArray,String[] namesArray){
        this.activity = activity;
        this.imagesArray = imagesArray;
        this.namesArray = namesArray;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = ((Activity)activity).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.slide_imkage, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.iv_auto_image_slider);
        imageView.setImageResource(imagesArray[position]);
        ((ViewPager)container).addView(viewItem);
        return viewItem;
    }
    @Override
    public int getCount() {
          // TODO Auto-generated method stub
         // TODO Auto-generated method stub
         return imagesArray.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((View)object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }
}