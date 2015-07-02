package com.example.myinstagram.adapters;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.myinstagram.utils.ImageUtils;
import com.example.myinstagram.utils.ViewHolder;

import java.util.List;

/**
 * Created by siddharth on 2/7/15.
 */
public class GridImageAdapter extends ImageAdapter {

    public GridImageAdapter(Context c, List<String> imageURLs) {
        super(c, imageURLs);
    }

    @Override
    protected ImageView getImageView(final Context context, final List<String> imageURLs) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(320, 320));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = ((ViewHolder) v.getTag()).position;
                ImageUtils.zoomImageFromThumb(v, position, imageURLs, context);
            }
        });
        return imageView;
    }

}
