package com.example.caipengli.helloworld;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

    private Point mSelectStartPoint;
    private Point mSelectEndPoint;
    private ContentItem mFirstSelectItem;
    private long timeChangeMark = -1;

    private List<ContentItem> mItems;
    private boolean [] preSelectStatus;


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
        mViewHeight = (int) (400 * density);
        mContentHeight = (int) (3000 * density);
        mScrollDy = (int) (10 * density);
        mContentTextSize = (int) (CONTENT_TEXT_SIZE_DP * density);

        mItems = StaticData.getContentItem(StaticData.getContentStrings(), getContext(), CONTENT_TEXT_SIZE_DP, 0, mScrollDy,
                mScreenWidth, 2, 2,
                5, 3,
                10, 10);
        preSelectStatus = new boolean[mItems.size()];
        resetPreStatus();
        int mostBottom = mItems.get(mItems.size() - 1).bottom;
        mContentHeight = mostBottom > mContentHeight ? mostBottom : mContentHeight;

        mSelectStartPoint = new Point();
        mSelectEndPoint = new Point();

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
                mSelectStartPoint.set((int)event.getX(), (int)event.getY() + getScrollY());
                mFirstSelectItem = getDownItem((int)event.getX(), (int)event.getY() + getScrollY());
                if (mFirstSelectItem != null) {
                    mFirstSelectItem.isSelect = !mFirstSelectItem.isSelect;
                    preSelectStatus[mFirstSelectItem.index] = mFirstSelectItem.isSelect;
                    timeChangeMark = System.currentTimeMillis();
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                mSelectEndPoint.set((int)event.getX(), (int)event.getY() + getScrollY());
                if (mFirstSelectItem != null) {
                    boolean upToDown = true;
                    if (mFirstSelectItem.left < mSelectEndPoint.x && mSelectEndPoint.x < mFirstSelectItem.right
                            && mFirstSelectItem.top < mSelectEndPoint.y && mSelectEndPoint.y < mFirstSelectItem.bottom) {
                        upToDown = true;
                    } else {
                        if (mSelectEndPoint.y < mFirstSelectItem.top) {
                            upToDown = false;
                        } else if (mFirstSelectItem.bottom < mSelectEndPoint.y) {
                            upToDown = true;
                        } else if (mSelectEndPoint.x < mFirstSelectItem.left) {
                            upToDown = false;
                        } else {
                            upToDown = true;
                        }
                        checkSelectStatus(upToDown);
                    }
                } else {
                    mFirstSelectItem = getDownItem((int)event.getX(), (int)event.getY() + getScrollY());
                    if (mFirstSelectItem != null) {
                        mFirstSelectItem.isSelect = !mFirstSelectItem.isSelect;
                        preSelectStatus[mFirstSelectItem.index] = mFirstSelectItem.isSelect;
                        timeChangeMark = System.currentTimeMillis();
                    }
                }
//                Log.i("cpl!", "getx  : " + event.getX() + "    getY : " + event.getY() + " getScrollY : " + getScrollY() + "   fis : " + mFirstSelectItem.isSelect);
                if (getScrollY()  + mViewHeight < mContentHeight && event.getRawY() > mTopInScreen + mViewHeight - BOTTOM_AREA_HEIGHT) {
                    mScroller.startScroll((int)getX(), mOffsetY, 0, mScrollDy);
                    mOffsetY += mScrollDy;
                } else if (getScrollY() > mScrollDy && event.getRawY() < mTopInScreen + TOP_AREA_HEIGHT) {
                    mScroller.startScroll((int)getX(), mOffsetY, 0, -Math.min(mScrollDy, getScrollY()));
                    mOffsetY -= mScrollDy;
                }
                break;
            case MotionEvent.ACTION_UP:
                mFirstSelectItem = null;
                mSelectStartPoint.set(0, 0);
                mSelectEndPoint.set(0, 0);
                resetPreStatus();
                break;
        }
        invalidate();
        return true;
    }

    private ContentItem getDownItem(int x, int y) {
        int closestDis = Integer.MAX_VALUE;
        int closeIndex = -1;
        int i = 0;
        for (ContentItem it : mItems)  {
            if (it.left < x && x < it.right && it.top < y && y < it.bottom) {
                return it;
            }
//            int dx = (it.left + it.right) / 2 - x;
//            int dy = (it.top + it.bottom) / 2 - y;
//            if (closestDis > dx * dx + dy * dy) {
//                closestDis = dx * dx + dy * dy;
//                closeIndex = i;
//            }
//            i++;
        }
//        return mItems.get(closeIndex);
        return null;
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
            if (item.isSelect) mPaint.setColor(Color.YELLOW);
            else mPaint.setColor(Color.GRAY);
            canvas.drawRect(item.left, item.top, item.right, item.bottom, mPaint);
            mPaint.setColor(Color.BLUE);
            canvas.drawText(item.display, item.textLeft, item.textBaseLine, mPaint);

        }
    }

    private void checkSelectStatus(boolean topToBottom) {
        if (topToBottom) {
            for (ContentItem item : mItems) {
                if (mSelectStartPoint.y < item.top && item.bottom < mSelectEndPoint.y) {//inside
                    item.isSelect = mFirstSelectItem.isSelect;
                } else if (item.top < mSelectStartPoint.y && mSelectStartPoint.y < item.bottom
                        && mSelectStartPoint.x < item.right && item.right < mSelectEndPoint.x) {//up side bounder
                    item.isSelect = mFirstSelectItem.isSelect;
                } else if (item.top < mSelectEndPoint.y && mSelectEndPoint.y < item.bottom
                        && item.left < mSelectEndPoint.x) {//bottom side bounder
                    item.isSelect = mFirstSelectItem.isSelect;
                } else {
                    item.isSelect = preSelectStatus[item.index];
                }
            }
        } else {//bottomToUp
            for (ContentItem item : mItems) {
                if (mSelectEndPoint.y < item.top && item.bottom < mSelectStartPoint.y) {//inside
                    item.isSelect = mFirstSelectItem.isSelect;
                } else if (item.top < mSelectEndPoint.y && mSelectEndPoint.y < item.bottom
                        && mSelectEndPoint.x < item.right && item.right < mSelectStartPoint.x) {//up side bounder
                    item.isSelect = mFirstSelectItem.isSelect;
                } else if (item.top < mSelectStartPoint.y && mSelectStartPoint.y < item.bottom
                        && item.left < mSelectStartPoint.x) {//bottom side bounder
                    item.isSelect = mFirstSelectItem.isSelect;
                } else {
                    item.isSelect = preSelectStatus[item.index];
                }
            }
        }
    }


    private void resetPreStatus() {
        for (ContentItem it : mItems) preSelectStatus[it.index] = it.isSelect;
    }
}
