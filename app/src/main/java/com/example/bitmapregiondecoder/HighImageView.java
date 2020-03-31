package com.example.bitmapregiondecoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Tudu.Zhao
 * on 2020/3/31
 **/

public class HighImageView extends View {

    private int mImageWidth;
    private int mImageHeight;
    private BitmapRegionDecoder mDecoder;
    private static BitmapFactory.Options mDecodeOptions = new BitmapFactory.Options();

    static {
        mDecodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    private Rect mRect = new Rect();
    private CustomGestureDetector customGestureDetector;
    private static final String TAG = "HighImageView";

    public HighImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setImage(InputStream is, int width, int height) {
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            mImageWidth = width;
            mImageHeight = height;

            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void init() {
        customGestureDetector = new CustomGestureDetector(getContext(), new OnGestureListener() {
            @Override
            public void onDrag(float dx, float dy) {
                if (mImageWidth > getWidth()) {
                    mRect.offset((int) -dx, 0);
                    checkWidth();
                    invalidate();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, (int) -dy);
                    checkHeight();
                    invalidate();
                }
            }

            @Override
            public void onFling(float startX, float startY, float velocityX, float velocityY) {

            }

            @Override
            public void onScale(float scaleFactor, float focusX, float focusY) {
                Log.d(TAG, "onScale");
            }
        });
    }

    private void checkHeight() {
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mRect.bottom - getHeight();
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = mRect.top + getHeight();
        }
    }

    private void checkWidth() {
        if (mRect.right > mImageWidth) {
            mRect.right = mImageWidth;
            mRect.left = mImageWidth - getWidth();
        }
        if (mRect.left < 0) {
            mRect.left = 0;
            mRect.right = mRect.left + getWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        customGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = mDecoder.decodeRegion(mRect, mDecodeOptions);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}