package com.example.caipengli.helloworld;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by caipengli on 17/7/16.
 */

public class StaticData {
    public static final String STANDARD_CHINESE = "中";
    public static String CONTENT = "整个Scroller用法的代码都在这里了，代码并不长，一共才100多行，我们一点点来看。 \n" +
            "首先在ScrollerLayout的构造函数里面我们进行了上述步骤中的第一步操作，即创建Scroller的实例，由于Scroller的实例只需创建一次，因此我们把它放到构造函数里面执行。另外在构建函数中我们还初始化的TouchSlop的值，这个值在后面将用于判断当前用户的操作是否是拖动。 \n" +
            "接着重写onMeasure()方法和onLayout()方法，在onMeasure()方法中测量ScrollerLayout里的每一个子控件的大小，在onLayout()方法中为ScrollerLayout里的每一个子控件在水平方向上进行布局。如果有朋友对这两个方法的作用还不理解，可以参照我之前写的一篇文章 Android视图绘制流程完全解析，带你一步步深入了解View(二) 。 \n" +
            "接着重写onInterceptTouchEvent()方法， 在这个方法中我们记录了用户手指按下时的X坐标位置，以及用户手指在屏幕上拖动时的X坐标位置，当两者之间的距离大于TouchSlop值时，就认为用户正在拖动布局，然后我们就将事件在这里拦截掉，阻止事件传递到子控件当中。 \n" +
            "那么当我们把事件拦截掉之后，就会将事件交给ScrollerLayout的onTouchEvent()方法来处理。如果当前事件是ACTION_MOVE，说明用户正在拖动布局，那么我们就应该对布局内容进行滚动从而影响拖动事件，实现的方式就是使用我们刚刚所学的scrollBy()方法，用户拖动了多少这里就scrollBy多少。另外为了防止用户拖出边界这里还专门做了边界保护，当拖出边界时就调用scrollTo()方法来回到边界位置。 \n" +
            "如果当前事件是ACTION_UP时，说明用户手指抬起来了，但是目前很有可能用户只是将布局拖动到了中间，我们不可能让布局就这么停留在中间的位置，因此接下来就需要借助Scroller来完成后续的滚动操作。首先这里我们先根据当前的滚动位置来计算布局应该继续滚动到哪一个子控件的页面，然后计算出距离该页面还需滚动多少距离。接下来我们就该进行上述步骤中的第二步操作，调用startScroll()方法来初始化滚动数据并刷新界面。startScroll()方法接收四个参数，第一个参数是滚动开始时X的坐标，第二个参数是滚动开始时Y的坐标，第三个参数是横向滚动的距离，正值表示向左滚动，第四个参数是纵向滚动的距离，正值表示向上滚动。紧接着调用invalidate()方法来刷新界面。 \n" +
            "现在前两步都已经完成了，最后我们还需要进行第三步操作，即重写computeScroll()方法，并在其内部完成平滑滚动的逻辑 。在整个后续的平滑滚动过程中，computeScroll()方法是会一直被调用的，因此我们需要不断调用Scroller的computeScrollOffset()方法来进行判断滚动操作是否已经完成了，如果还没完成的话，那就继续调用scrollTo()方法，并把Scroller的curX和curY坐标传入，然后刷新界面从而完成平滑滚动的操作。 \n" +
            "现在ScrollerLayout已经准备好了，接下来我们修改activity_main.xml布局中的内容，如下所示：";

    public static List<String> getContentStrings() {
        List<String> list = new LinkedList<String>();
        int totalLen = CONTENT.length();
        for (int i = 0; i < totalLen;) {
            int rand = (int) (Math.random() * 25) + 1;
            if (i + rand <= totalLen) {
                list.add(CONTENT.substring(i, i + rand));
            } else {
                list.add(CONTENT.substring(i, totalLen));
            }
            i += rand;
        }
        return list;
    }

    public static List<ContentItem> getContentItem(List<String> strs,
                                            Context context, int textSizeDp,
                                            int firX, int firY,
                                            int totalWidth, int itemXPaddingDp, int itemYPaddingDp,
                                            int rowGapDp, int colGapDp,
                                            int leftPaddingDp, int rightPaddingDp) {
        if (strs == null) return null;
        float density = context.getResources().getDisplayMetrics().density;
        float lp = leftPaddingDp * density;
        float rp = rightPaddingDp * density;
        int rg = (int) (rowGapDp * density);
        int cg = (int) (colGapDp * density);
        float textSize = textSizeDp * density;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        float fontHeight = paint.getFontMetrics().bottom - paint.getFontMetrics().top;
        float textXPadding = itemXPaddingDp * density;
        float textYPadding = itemYPaddingDp * density;
        float textBaseLine = fontHeight - paint.getFontMetrics().descent;
        int itemHeight = (int) (2 * textYPadding + fontHeight);
        List<ContentItem> list = new ArrayList<ContentItem>();
        int curX = firX;
        int curY = firY;
        totalWidth = (int) (totalWidth - lp - rp);
        int maxNum = (int) (totalWidth / paint.measureText(STANDARD_CHINESE));
        int index = 0;
        for (String str : strs) {
            ContentItem item = new ContentItem();
            item.index = index++;
            item.content = str;
            int wordLen = (int) (paint.measureText(str) + textXPadding * 2);
            if (wordLen >= totalWidth) {
                item.display = str.substring(0, maxNum - 3 >= 0 ? maxNum - 3 : maxNum) + "...";
                item.left = leftPaddingDp;
                item.right = totalWidth - rightPaddingDp;
                if (curX > leftPaddingDp) { // this row had been used, let it in next line
                    curY += itemHeight + rg;
                }
                item.top = curY;
                item.bottom = item.top + itemHeight;
                curY += itemHeight + rg;
                curX = leftPaddingDp;
            } else {
                item.display = str;
                if (curX + cg + wordLen < totalWidth - rp) {// put it in this line
                    if (curX != leftPaddingDp) curX += cg;//no the first item
                } else {//next line
                    curX = leftPaddingDp;
                    curY += itemHeight + rg;
                }
                item.left = curX;
                item.right = item.left + wordLen;
                item.top = curY;
                item.bottom = item.top + itemHeight;
                curX = item.right;
            }
            item.textLeft = (int) (item.left + textXPadding);
            item.textBaseLine = (int) (item.top + textBaseLine);
            item.isSelect = false;
            list.add(item);
        }
        return list;
    }
}
