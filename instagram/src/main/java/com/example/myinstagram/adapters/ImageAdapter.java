package com.example.myinstagram.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.myinstagram.utils.Constants;
import com.example.myinstagram.utils.ImageUtils;
import com.example.myinstagram.utils.ViewHolder;

import java.util.List;

public abstract class ImageAdapter extends BaseAdapter {

    final static int reqWidth = 320;
    final static int reqHeight = 320;

    private Context mContext;
    private List<String> mImageURLs;

    public ImageAdapter(Context c, List<String> imageURLs) {
        mContext = c;
        mImageURLs = imageURLs;
    }

    public void addImageURL(String imageURL) {
        mImageURLs.add(imageURL);
    }

    public int getCount() {
        if (mImageURLs == null || mImageURLs.isEmpty())
            return Constants.drawables.length;
        return mImageURLs.size();
    }

    public Object getItem(int position) {
        if (mImageURLs == null || mImageURLs.isEmpty())
            return Constants.drawables[position];
        return mImageURLs.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            imageView = getImageView(mContext, mImageURLs);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(null);
        viewHolder.image = imageView;
        viewHolder.position = position;
        imageView.setTag(viewHolder);
        new LazyLoadImages().execute(viewHolder);

        return imageView;
    }

    private class LazyLoadImages extends AsyncTask<ViewHolder, Void, Bitmap> {

        private ViewHolder mViewHolder;


        @Override
        protected Bitmap doInBackground(ViewHolder... viewHolders) {
            mViewHolder = viewHolders[0];
            int position = mViewHolder.position;

            Resources res = mContext.getResources();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            if (mImageURLs != null && !mImageURLs.isEmpty())
                BitmapFactory.decodeFile(mImageURLs.get(position), options);
            else
                BitmapFactory.decodeResource(res, Constants.drawables[position], options);
            options.inSampleSize = ImageUtils.calculateInSampleSize(options, reqHeight, reqWidth);
            options.inJustDecodeBounds = false;
            if (mImageURLs != null && !mImageURLs.isEmpty())
                return BitmapFactory.decodeFile(mImageURLs.get(position), options);
            else
                return BitmapFactory.decodeResource(res, Constants.drawables[position], options);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mViewHolder.image.setImageBitmap(result);
        }

    }

    protected abstract ImageView getImageView(Context context, List<String> imageURLs);

}