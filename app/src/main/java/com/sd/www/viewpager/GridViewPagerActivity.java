package com.sd.www.viewpager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.viewpager.FGridViewPager;
import com.sd.www.viewpager.adapter.GridAdapter;
import com.sd.www.viewpager.model.DataModel;

public class GridViewPagerActivity extends AppCompatActivity
{
    private FGridViewPager mViewPager;
    private final GridAdapter mAdapter = new GridAdapter();

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_grid_viewpager);
        mViewPager = findViewById(R.id.vpg_content);

        /**
         * 设置每页要显示的item数量
         */
        mViewPager.setGridItemCountPerPage(9);
        /**
         * 设置每页的数据要按几列展示
         */
        mViewPager.setGridColumnCountPerPage(3);
        /**
         * 设置横分割线
         */
        mViewPager.setGridHorizontalDivider(getResources().getDrawable(R.drawable.divider_horizontal));
        /**
         * 设置竖分割线
         */
        mViewPager.setGridVerticalDivider(getResources().getDrawable(R.drawable.divider_vertical));
        /**
         * 设置适配器
         */
        mViewPager.setGridAdapter(mAdapter);

        mAdapter.getDataHolder().setData(DataModel.get(20));
    }

    public void onClickRemove(View view)
    {
        mAdapter.getDataHolder().removeData(0);

        final String selected = mAdapter.getSelectManager().getMode().isSingleType() ?
                String.valueOf(mAdapter.getSelectManager().getSelectedItem())
                : String.valueOf(mAdapter.getSelectManager().getSelectedItems());

        Toast.makeText(this, selected, Toast.LENGTH_SHORT).show();
    }
}
