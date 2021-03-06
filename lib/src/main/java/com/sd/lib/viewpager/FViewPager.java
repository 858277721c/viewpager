package com.sd.lib.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FViewPager extends ViewPager
{
    public FViewPager(Context context)
    {
        super(context);
    }

    public FViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    private Map<PullCondition, String> mPullConditionHolder;
    private HeightMode mHeightMode = HeightMode.max_child;
    private int mMaxChildHeightHistory;

    /**
     * 设置高度模式
     *
     * @param mode
     */
    public void setHeightMode(HeightMode mode)
    {
        if (mode == null)
            return;

        if (mHeightMode != mode)
        {
            mHeightMode = mode;
            requestLayout();
        }
    }

    /**
     * 重置历史子View最高值
     */
    public void resetMaxChildHeightHistory()
    {
        mMaxChildHeightHistory = 0;
    }

    /**
     * 添加拖动条件
     *
     * @param condition
     */
    public void addPullCondition(PullCondition condition)
    {
        if (condition == null)
            return;

        if (mPullConditionHolder == null)
            mPullConditionHolder = new ConcurrentHashMap<>();

        mPullConditionHolder.put(condition, "");
    }

    /**
     * 是否包含某个拖动条件
     *
     * @param condition
     * @return
     */
    public boolean containsPullCondition(PullCondition condition)
    {
        if (condition == null)
            return false;

        if (mPullConditionHolder == null)
            return false;

        return mPullConditionHolder.containsKey(condition);
    }

    /**
     * 移除拖动条件
     *
     * @param condition
     */
    public void removePullCondition(PullCondition condition)
    {
        if (condition == null)
            return;

        if (mPullConditionHolder == null)
            return;

        mPullConditionHolder.remove(condition);
        if (mPullConditionHolder.isEmpty())
            mPullConditionHolder = null;
    }

    private boolean canPull(MotionEvent event)
    {
        if (mPullConditionHolder == null || mPullConditionHolder.isEmpty())
            return true;

        for (PullCondition item : mPullConditionHolder.keySet())
        {
            if (!item.canPull(event, this))
                return false;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (!canPull(ev))
            return false;

        try
        {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = false;

        try
        {
            result = super.onTouchEvent(event);
        } catch (IllegalArgumentException ex)
        {
            ex.printStackTrace();
        }

        if (!canPull(event))
            return false;

        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (mHeightMode == HeightMode.max_child || mHeightMode == HeightMode.max_child_history)
        {
            final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            if (heightMode != MeasureSpec.EXACTLY)
            {
                int maxHeight = 0;
                final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                final int count = getChildCount();
                for (int i = 0; i < count; i++)
                {
                    final View child = getChildAt(i);
                    child.measure(widthMeasureSpec, childHeightMeasureSpec);

                    final int height = child.getMeasuredHeight();
                    if (height > maxHeight)
                        maxHeight = height;
                }

                if (maxHeight > mMaxChildHeightHistory)
                    mMaxChildHeightHistory = maxHeight;

                if (heightMode == MeasureSpec.AT_MOST)
                    maxHeight = Math.min(maxHeight, MeasureSpec.getSize(heightMeasureSpec));

                if (mHeightMode == HeightMode.max_child)
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
                else
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxChildHeightHistory, MeasureSpec.EXACTLY);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface PullCondition
    {
        /**
         * 返回是否可以触发拖动
         *
         * @param event
         * @param viewPager
         * @return
         */
        boolean canPull(MotionEvent event, FViewPager viewPager);
    }

    public enum HeightMode
    {
        /** 普通模式 */
        normal,
        /** 最大子View高度 */
        max_child,
        /** 历史最大子View高度 */
        max_child_history
    }
}
