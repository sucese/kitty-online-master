package com.guoxiaoxing.kitty.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.GoodsDisplayAdapter;
import com.guoxiaoxing.kitty.model.Goods;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;

import butterknife.Bind;

/**
 *
 */
public class GoodsDisplayFragment extends BaseFragment {

    private Goods mGoods;

    public GoodsDisplayFragment(){

    }

    public void setData(Goods goods){
        mGoods = goods;
    }

    public static GoodsDisplayFragment newInstance() {
        Bundle args = new Bundle();
        GoodsDisplayFragment fragment = new GoodsDisplayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.rv_goods_display)
    RecyclerView mRvGoodsDisplay;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_display;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mRvGoodsDisplay.setLayoutManager(new LinearLayoutManager(getActivity()));
        GoodsDisplayAdapter adapter = new GoodsDisplayAdapter();
        adapter.setData(mGoods);
        mRvGoodsDisplay.setAdapter(adapter);

    }

    @Override
    public void initData() {
        super.initData();
    }
}
