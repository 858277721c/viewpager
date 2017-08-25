package com.fanwe.library.viewpager.indicator.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.fanwe.library.viewpager.indicator.IPagerIndicatorItemView;
import com.fanwe.library.viewpager.indicator.model.PositionData;

/**
 * Created by Administrator on 2017/8/24.
 */
public abstract class PagerIndicatorItemView extends FrameLayout implements IPagerIndicatorItemView
{
    public PagerIndicatorItemView(Context context)
    {
        super(context);
        init();
    }

    public PagerIndicatorItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PagerIndicatorItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private PositionData mPositionData;

    private void init()
    {
    }

    @Override
    public void onShowPercent(float showPercent, boolean isEnter, boolean isMoveLeft)
    {

    }

    @Override
    public PositionData getPositionData()
    {
        if (mPositionData == null)
        {
            mPositionData = new PositionData();
        }
        return mPositionData;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        getPositionData().left = getLeft();
        getPositionData().top = getTop();
        getPositionData().right = getRight();
        getPositionData().bottom = getBottom();
    }
}
