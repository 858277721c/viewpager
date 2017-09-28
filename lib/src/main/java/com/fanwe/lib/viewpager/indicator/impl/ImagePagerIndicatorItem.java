/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.viewpager.indicator.impl;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanwe.lib.viewpager.R;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.model.PositionData;

public class ImagePagerIndicatorItem extends FrameLayout implements IPagerIndicatorItem
{
    public ImagePagerIndicatorItem(Context context)
    {
        super(context);
        init();
    }

    public ImagePagerIndicatorItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ImagePagerIndicatorItem(Context context, AttributeSet attrs, int defStyleAttr)
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
        addView(mImageView);

        onSelectedChanged(false);
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
        FrameLayout.LayoutParams params = (LayoutParams) getImageView().getLayoutParams();
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

        final int margin = getIndicatorConfig().margin / 2;
        params.leftMargin = margin;
        params.rightMargin = margin;

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
