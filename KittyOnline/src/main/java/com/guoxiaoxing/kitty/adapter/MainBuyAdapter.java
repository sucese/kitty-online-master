package com.guoxiaoxing.kitty.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.base.BaseMultipleItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 买买买页商品展示Adapter
 *
 * @author guoxiaoxing
 */

public class MainBuyAdapter extends BaseMultipleItemAdapter {

    private List<String> mGoodsList = new ArrayList<>();

    public MainBuyAdapter() {
        mHeaderCount = 1;
        mBottomCount = 1;
    }

    public void setData(ArrayList<String> list) {
        mGoodsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buy_fragment_content, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buy_fragment_header, parent, false);
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ContentViewHolder) {


        } else if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof BottomViewHolder) {

        }
    }

    @Override
    public int getContentItemCount() {
        return 4;
    }


    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ll_goods)
        LinearLayout mLlGoods;

        ContentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class BottomViewHolder extends RecyclerView.ViewHolder {

        BottomViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}