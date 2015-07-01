package com.example.myinstagram.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.myinstagram.com.example.myinstagram.utils.Constants;
import com.example.myinstagram.instagram.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {

    final static int reqWidth = 320;
    final static int reqHeight = 320;

    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;

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
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(320, 320));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position;
                    if (mImageURLs != null && !mImageURLs.isEmpty())
                        position = ((ViewHolder) v.getTag()).position;
                    else
                        position = Constants.drawables[((ViewHolder) v.getTag()).position];
                    zoomImageFromThumb(v, position);
                }
            });

            convertView = imageView;

            viewHolder.image = imageView;
            viewHolder.position = position;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            ((ImageView) convertView).setImageBitmap(null);
        }

        new MyGridView().execute(viewHolder);

        return convertView;
    }

    class ViewHolder {
        ImageView image;
        int position;
    }

    private class MyGridView extends AsyncTask<ViewHolder, Integer, Bitmap> {

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
            options.inSampleSize = calculateInSampleSize(options);
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

    public static int calculateInSampleSize(
            BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void zoomImageFromThumb(final View thumbView, int position) {
        // If there's an animation in progress, cancel it immediately and
        // proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) ((Activity) mContext)
                .findViewById(R.id.expanded_image);
        if (mImageURLs != null && !mImageURLs.isEmpty())
            expandedImageView.setImageBitmap(BitmapFactory.decodeFile(mImageURLs.get(position)));
        else
            expandedImageView.setImageResource(position);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the
        // final bounds are the global visible rectangle of the container view.
        // Also
        // set the container view's offset as the origin for the bounds, since
        // that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        ((Activity) mContext).findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the
        // "center crop" technique. This prevents undesirable stretching during
        // the animation.
        // Also calculate the start scaling factor (the end scaling factor is
        // always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
                .width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the
        // top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set.play(
                ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y,
                        startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down to the
        // original bounds
        // and show the thumbnail instead of the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set.play(
                        ObjectAnimator.ofFloat(expandedImageView, View.X,
                                startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                                startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView,
                                View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView,
                                View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}