package com.example.caipengli.helloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by caipengli on 17/7/16.
 */

public class ScrollerSelectView extends View {
    private static final int CONTENT_TEXT_SIZE_DP = 18;
    private static float BOTTOM_AREA_HEIGHT = 50;
    private static float TOP_AREA_HEIGHT = 50;

    private Scroller mScroller;
    private Paint mPaint;

    private int mScreenHeight = 1920;
    private int mScreenWidth = 1080;
    private int mViewHeight = 3000;
    private int mContentHeight = 3000;
    private int mScrollDy = 10;
    private int mOffsetY = 0;
    private int mTopInScreen = -1;
    private int mBottomInScreen = -1;
    private int mContentTextSize = 54;

    private List<ContentItem> mItems;

    public ScrollerSelectView(Context context) {
        super(context);
        init();
    }

    public ScrollerSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        float density = getContext().getResources().getDisplayMetrics().density;
        BOTTOM_AREA_HEIGHT = BOTTOM_AREA_HEIGHT * density;
        TOP_AREA_HEIGHT = BOTTOM_AREA_HEIGHT;
        mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mViewHeight = (int) (500 * density);
        mContentHeight = (int) (3000 * density);
        mScrollDy = (int) (10 * density);
        mContentTextSize = (int) (CONTENT_TEXT_SIZE_DP * density);

        mItems = StaticData.getContentItem(StaticData.getContentStrings(), getContext(), CONTENT_TEXT_SIZE_DP, 0, mScrollDy,
                mScreenWidth, 2, 2,
                5, 3,
                10, 10);
        int mostBottom = mItems.get(mItems.size() - 1).bottom;
        mContentHeight = mostBottom > mContentHeight ? mostBottom : mContentHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int h = MeasureSpec.makeMeasureSpec(mViewHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mTopInScreen == -1) {
            int [] loc = new int [2];
            getLocationOnScreen(loc);
            mTopInScreen = loc[1];
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                if (getScrollY()  + mViewHeight < mContentHeight && event.getRawY() > mTopInScreen + mViewHeight - BOTTOM_AREA_HEIGHT) {
                    mScroller.startScroll((int)getX(), mOffsetY, 0, mScrollDy);
                    mOffsetY += mScrollDy;
                } else if (getScrollY() > 0 && event.getRawY() < mTopInScreen + TOP_AREA_HEIGHT) {
                    mScroller.startScroll((int)getX(), mOffsetY, 0, -mScrollDy);
                    mOffsetY -= mScrollDy;
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int curH = 0; curH < mContentHeight; curH += 200) {
            if (curH % 400 == 0) mPaint.setColor(Color.GREEN);
            else mPaint.setColor(Color.RED);
            canvas.drawRect(getX(), curH, mScreenWidth, curH + 200, mPaint);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextSize(80);
            canvas.drawText((curH / 200) + "", getX(), curH + 200, mPaint);
        }

        drawItems(canvas);
    }

    private void drawItems(Canvas canvas) {
        mPaint.setTextSize(mContentTextSize);
        for (ContentItem item : mItems) {
            mPaint.setColor(Color.YELLOW);
            canvas.drawRect(item.left, item.top, item.right, item.bottom, mPaint);
            mPaint.setColor(Color.BLUE);
            canvas.drawText(item.display, item.textLeft, item.textBaseLine, mPaint);
        }
    }

}
