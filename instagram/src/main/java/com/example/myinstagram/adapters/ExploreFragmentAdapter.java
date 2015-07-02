package com.example.myinstagram.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by bharat on 1/7/15.
 */
public class ExploreFragmentAdapter extends ImageAdapter {


    public ExploreFragmentAdapter(Context c, List<String> imageUrls){
        super(c, imageUrls);
    }

    @Override
    protected ImageView getImageView(Context context, List<String> imageURLs) {
        ImageView imgView =  new ImageView(context);
        imgView.setMaxHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        imgView.setMaxWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        return imgView;
    }
}
