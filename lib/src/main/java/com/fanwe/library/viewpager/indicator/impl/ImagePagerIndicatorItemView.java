package com.fanwe.library.viewpager.indicator.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanwe.library.viewpager.R;
import com.fanwe.library.viewpager.indicator.IPagerIndicatorItemView;
import com.fanwe.library.viewpager.indicator.model.PositionData;

/**
 * Created by Administrator on 2017/8/24.
 */
public class ImagePagerIndicatorItemView extends FrameLayout implements IPagerIndicatorItemView
{
    public ImagePagerIndicatorItemView(Context context)
    {
        super(context);
        init();
    }

    public ImagePagerIndicatorItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ImagePagerIndicatorItemView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ImageView mImageView;
    private IndicatorConfig mIndicatorConfig;
    private PositionData mPositionData;

    private void init()
    {
        mImageView = new ImageView(getContext());
        initImageView(mImageView);
        addView(mImageView);

        onSelectedChanged(false);
    }

    protected void initImageView(ImageView imageView)
    {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int margin = getIndicatorConfig().margin / 2;
        params.leftMargin = margin;
        params.rightMargin = margin;
        imageView.setLayoutParams(params);
    }

    public ImageView getImageView()
    {
        return mImageView;
    }

    public IndicatorConfig getIndicatorConfig()
    {
        if (mIndicatorConfig == null)
        {
            mIndicatorConfig = new IndicatorConfig(getContext());
        }
        return mIndicatorConfig;
    }

    public void setIndicatorConfig(IndicatorConfig indicatorConfig)
    {
        mIndicatorConfig = indicatorConfig;
    }

    @Override
    public void onSelectedChanged(boolean selected)
    {
        ViewGroup.LayoutParams params = getImageView().getLayoutParams();
        if (selected)
        {
            params.width = getIndicatorConfig().widthSelected;
            params.height = getIndicatorConfig().heightSelected;

            getImageView().setImageResource(getIndicatorConfig().imageResIdSelected);
        } else
        {
            params.width = getIndicatorConfig().widthNormal;
            params.height = getIndicatorConfig().heightNormal;

            getImageView().setImageResource(getIndicatorConfig().imageResIdNormal);
        }
        getImageView().setLayoutParams(params);
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

    public static class IndicatorConfig
    {
        public int imageResIdNormal;
        public int imageResIdSelected;

        public int widthNormal;
        public int heightNormal;

        public int widthSelected;
        public int heightSelected;

        public int margin;

        public IndicatorConfig(Context context)
        {
            this.imageResIdNormal = R.drawable.lib_vpg_ic_indicator_normal;
            this.imageResIdSelected = R.drawable.lib_vpg_ic_indicator_selected;

            this.widthNormal = context.getResources().getDimensionPixelSize(R.dimen.lib_vpg_indicator_width_normal);
            this.heightNormal = context.getResources().getDimensionPixelSize(R.dimen.lib_vpg_indicator_height_normal);

            this.widthSelected = context.getResources().getDimensionPixelSize(R.dimen.lib_vpg_indicator_width_selected);
            this.heightSelected = context.getResources().getDimensionPixelSize(R.dimen.lib_vpg_indicator_height_selected);

            this.margin = context.getResources().getDimensionPixelSize(R.dimen.lib_vpg_indicator_margin);
        }
    }
}
