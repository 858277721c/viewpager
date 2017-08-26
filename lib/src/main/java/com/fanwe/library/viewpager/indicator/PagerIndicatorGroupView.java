package com.fanwe.library.viewpager.indicator;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.viewpager.helper.SDViewPagerInfoListener;
import com.fanwe.library.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.library.viewpager.indicator.impl.ImagePagerIndicatorItemView;

/**
 * ViewPager指示器GroupView
 */
public abstract class PagerIndicatorGroupView extends LinearLayout implements IPagerIndicatorGroupView
{
    public PagerIndicatorGroupView(Context context)
    {
        super(context);
        init();
    }

    public PagerIndicatorGroupView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public PagerIndicatorGroupView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static final String TAG = "PagerIndicatorGroupView";

    private PagerIndicatorAdapter mAdapter;
    private IPagerIndicatorTrackView mPagerIndicatorTrackView;

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
                PagerIndicatorGroupView.this.onPageCountChanged(count);
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
                PagerIndicatorGroupView.this.onSelectedChanged(position, selected);
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
                PagerIndicatorGroupView.this.onShowPercent(position, showPercent, isEnter, isMoveLeft);
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

    @Override
    public void setPagerIndicatorTrackView(IPagerIndicatorTrackView pagerIndicatorTrackView)
    {
        mPagerIndicatorTrackView = pagerIndicatorTrackView;
    }

    @Override
    public IPagerIndicatorTrackView getPagerIndicatorTrackView()
    {
        return mPagerIndicatorTrackView;
    }

    private PagerIndicatorAdapter mInternalPagerIndicatorAdapter = new PagerIndicatorAdapter()
    {
        @Override
        public IPagerIndicatorItemView onCreateView(int position, ViewGroup viewParent)
        {
            return new ImagePagerIndicatorItemView(getContext());
        }
    };

    @Override
    public void onPageCountChanged(int count)
    {
        if (getPagerIndicatorTrackView() != null)
        {
            getPagerIndicatorTrackView().onPageCountChanged(count);
        }
    }

    @Override
    public void onShowPercent(int position, float showPercent, boolean isEnter, boolean isMoveLeft)
    {
        IPagerIndicatorItemView itemView = getItemView(position);
        if (itemView != null)
        {
            itemView.onShowPercent(showPercent, isEnter, isMoveLeft);

            if (getPagerIndicatorTrackView() != null)
            {
                getPagerIndicatorTrackView().onShowPercent(position, showPercent, isEnter, isMoveLeft, itemView.getPositionData());
            }
        }
    }

    @Override
    public void onSelectedChanged(int position, boolean selected)
    {
        IPagerIndicatorItemView itemView = getItemView(position);
        if (itemView != null)
        {
            itemView.onSelectedChanged(selected);

            if (getPagerIndicatorTrackView() != null)
            {
                getPagerIndicatorTrackView().onSelectedChanged(position, selected, itemView.getPositionData());
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