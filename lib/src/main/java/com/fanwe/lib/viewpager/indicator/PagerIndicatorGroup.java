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
package com.fanwe.lib.viewpager.indicator;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.lib.viewpager.helper.SDViewPagerInfoListener;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.ImagePagerIndicatorItem;

/**
 * ViewPager指示器Group
 */
public abstract class PagerIndicatorGroup extends LinearLayout implements IPagerIndicatorGroup
{
    public PagerIndicatorGroup(Context context)
    {
        super(context);
        init();
    }

    public PagerIndicatorGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PagerIndicatorGroup(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static final String TAG = "PagerIndicatorGroup";

    private PagerIndicatorAdapter mAdapter;
    private IPagerIndicatorTrack mPagerIndicatorTrack;

    private SDViewPagerInfoListener mViewPagerInfoListener = new SDViewPagerInfoListener();

    /**
     * 当DataSetObserver变化的时候是否全部重新创建view
     */
    private boolean mIsFullCreateMode = true;

    private boolean mIsDebug;

    private void init()
    {
        setAdapter(mInternalPagerIndicatorAdapter);
        initViewPagerInfoListener();
    }

    public void setDebug(boolean debug)
    {
        mIsDebug = debug;
    }

    protected int getPageCount()
    {
        return mViewPagerInfoListener.getPageCount();
    }

    private void initViewPagerInfoListener()
    {
        mViewPagerInfoListener.setDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                //ViewPager的Adapter数据集变化通知
                onDataSetChangedInternal();
            }

            @Override
            public void onInvalidated()
            {
                super.onInvalidated();
            }
        });
        mViewPagerInfoListener.setOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener()
        {
            @Override
            public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter)
            {
                // Adapter变化通知
                onDataSetChangedInternal();
            }
        });
        mViewPagerInfoListener.setOnPageCountChangeCallback(new SDViewPagerInfoListener.OnPageCountChangeCallback()
        {
            @Override
            public void onPageCountChanged(int count)
            {
                if (mIsDebug)
                {
                    Log.i(TAG, "onPageCountChanged:" + count);
                }
                PagerIndicatorGroup.this.onPageCountChanged(count);
            }
        });
        mViewPagerInfoListener.setOnPageSelectedChangeCallback(new SDViewPagerInfoListener.OnPageSelectedChangeCallback()
        {
            @Override
            public void onSelectedChanged(int position, boolean selected)
            {
                if (mIsDebug)
                {
                    Log.i(TAG, "onSelectedChanged:" + position + "," + selected);
                }
                PagerIndicatorGroup.this.onSelectedChanged(position, selected);
            }
        });
        mViewPagerInfoListener.setOnPageScrolledPercentChangeCallback(new SDViewPagerInfoListener.OnPageScrolledPercentChangeCallback()
        {
            @Override
            public void onShowPercent(int position, float showPercent, boolean isEnter, boolean isMoveLeft)
            {
                if (mIsDebug)
                {
                    if (isEnter)
                    {
                        Log.i(TAG, "Enter  " + position + "  " + showPercent + "  " + isMoveLeft);
                    } else
                    {
                        Log.e(TAG, "Leave  " + position + "  " + showPercent + "  " + isMoveLeft);
                    }
                }
                PagerIndicatorGroup.this.onShowPercent(position, showPercent, isEnter, isMoveLeft);
            }
        });
    }

    @Override
    public void setViewPager(ViewPager viewPager)
    {
        mViewPagerInfoListener.setViewPager(viewPager);
    }

    @Override
    public ViewPager getViewPager()
    {
        return mViewPagerInfoListener.getViewPager();
    }

    @Override
    public void setAdapter(PagerIndicatorAdapter adapter)
    {
        if (mAdapter != null)
        {
            mAdapter.unregisterDataSetObserver(mInternalIndicatorAdapterDataSetObserver);
        }
        mAdapter = adapter;
        if (adapter != null)
        {
            adapter.registerDataSetObserver(mInternalIndicatorAdapterDataSetObserver);
        }
    }

    @Override
    public PagerIndicatorAdapter getAdapter()
    {
        return mAdapter;
    }

    @Override
    public void setFullCreateMode(boolean fullCreateMode)
    {
        mIsFullCreateMode = fullCreateMode;
    }

    @Override
    public boolean isFullCreateMode()
    {
        return mIsFullCreateMode;
    }

    public void setPagerIndicatorTrack(IPagerIndicatorTrack pagerIndicatorTrack)
    {
        mPagerIndicatorTrack = pagerIndicatorTrack;
    }

    public IPagerIndicatorTrack getPagerIndicatorTrack()
    {
        return mPagerIndicatorTrack;
    }

    private PagerIndicatorAdapter mInternalPagerIndicatorAdapter = new PagerIndicatorAdapter()
    {
        @Override
        public IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent)
        {
            return new ImagePagerIndicatorItem(getContext());
        }
    };

    @Override
    public void onPageCountChanged(int count)
    {
        if (getPagerIndicatorTrack() != null)
        {
            getPagerIndicatorTrack().onPageCountChanged(count);
        }
    }

    @Override
    public void onShowPercent(int position, float showPercent, boolean isEnter, boolean isMoveLeft)
    {
        IPagerIndicatorItem item = getPagerIndicatorItem(position);
        if (item != null)
        {
            item.onShowPercent(showPercent, isEnter, isMoveLeft);

            if (getPagerIndicatorTrack() != null)
            {
                getPagerIndicatorTrack().onShowPercent(position, showPercent, isEnter, isMoveLeft, item.getPositionData());
            }
        }
    }

    @Override
    public void onSelectedChanged(int position, boolean selected)
    {
        IPagerIndicatorItem item = getPagerIndicatorItem(position);
        if (item != null)
        {
            item.onSelectedChanged(selected);

            if (getPagerIndicatorTrack() != null)
            {
                getPagerIndicatorTrack().onSelectedChanged(position, selected, item.getPositionData());
            }
        }
    }

    private DataSetObserver mInternalIndicatorAdapterDataSetObserver = new DataSetObserver()
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            //指示器的Adapter数据集变化通知
            onDataSetChangedInternal();
        }

        @Override
        public void onInvalidated()
        {
            super.onInvalidated();
        }
    };

    private void onDataSetChangedInternal()
    {
        onDataSetChanged();
        mViewPagerInfoListener.notifySelected();
    }

    protected abstract void onDataSetChanged();

}
