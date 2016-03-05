package com.guoxiaoxing.kitty.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.base.BaseMultipleItemAdapter;
import com.guoxiaoxing.kitty.model.Goods;
import com.guoxiaoxing.kitty.widget.banner.ConvenientBanner;
import com.guoxiaoxing.kitty.widget.banner.holder.CBViewHolderCreator;
import com.guoxiaoxing.kitty.widget.banner.holder.NetworkImageHolderView;
import com.guoxiaoxing.kitty.widget.banner.transforms.FlipHorizontalTransformer;
import com.guoxiaoxing.kitty.widget.timecounter.CountdownView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 买买买页商品展示Adapter
 *
 * @author guoxiaoxing
 */

public class MainBuyAdapter extends BaseMultipleItemAdapter implements View.OnClickListener {


    private List<Goods> mGoodsList = new ArrayList<>();
    private String[] images = {
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_0.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_1.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_2.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_3.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_4.jpg"};

    @Override
    public void onClick(View v) {
        mOnRecyclerViewItemClickListener.onItemClick(v, (Goods) v.getTag());
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    public MainBuyAdapter() {
        mHeaderCount = 1;
    }

    private HeaderViewHolder mHeaderViewHolder;

    public void setData(ArrayList<Goods> list) {
        mGoodsList.addAll(list);
        mGoodsList.addAll(list);
        mGoodsList.addAll(list);
        mGoodsList.addAll(list);
        mGoodsList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buy_fragment_header, parent, false);
        HeaderViewHolder holder = new HeaderViewHolder(view);
        mHeaderViewHolder = holder;
        return holder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buy_fragment_content, parent, false);
        view.setOnClickListener(this);
        return new ContentViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {

            ((HeaderViewHolder) holder).mCbSaleAd.getViewPager().setPageTransformer(true,
                    new FlipHorizontalTransformer());
            ((HeaderViewHolder) holder).mCbSaleAd.setPages(new CBViewHolderCreator
                    <NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, Arrays.asList(images));
            mHeaderViewHolder.mCbSaleAd.startTurning(AppConfig.VIEWPAGER_TRANSFORM_TIME);


            ((HeaderViewHolder) holder).mCbSaleAd2.getViewPager().setPageTransformer(true,
                    new FlipHorizontalTransformer());
            ((HeaderViewHolder) holder).mCbSaleAd2.setPages(new CBViewHolderCreator
                    <NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, Arrays.asList(images));
            mHeaderViewHolder.mCbSaleAd2.startTurning(AppConfig.VIEWPAGER_TRANSFORM_TIME);


            long time2 = (long) 30 * 60 * 1000;
            ((HeaderViewHolder) holder).mCvSale.start(time2);

        } else if (holder instanceof ContentViewHolder) {

            ((ContentViewHolder) holder).mTvGoodsName.setText(mGoodsList.get(position - mHeaderCount).getGoodsName());
            ((ContentViewHolder) holder).mTvGoodsDesc.setText(mGoodsList.get(position - mHeaderCount).getGoodsDesc());
            ((ContentViewHolder) holder).mTvGoodsCategory.setText(mGoodsList.get(position - mHeaderCount).getGoodsCategory());
            ((ContentViewHolder) holder).mSdvGoodsImage.setImageURI(Uri.parse(mGoodsList.get(position - mHeaderCount).getGoodsImage()));

            holder.itemView.setTag(mGoodsList.get(position - mHeaderCount));


        } else if (holder instanceof BottomViewHolder) {

        }
    }

    @Override
    public int getContentItemCount() {
        return mGoodsList.size();
    }

    public void startBannerTurning() {
        if (mHeaderViewHolder != null) {
            mHeaderViewHolder.mCbSaleAd.startTurning(AppConfig.VIEWPAGER_TRANSFORM_TIME);
        }
    }

    public void stopBannerTurning() {
        if (mHeaderViewHolder != null) {
            mHeaderViewHolder.mCbSaleAd.stopTurning();
        }
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cv_sale)
        CountdownView mCvSale;
        @Bind(R.id.cb_sale_ad)
        ConvenientBanner mCbSaleAd;
        @Bind(R.id.cb_sale_ad2)
        ConvenientBanner mCbSaleAd2;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.sdv_goods_image)
        SimpleDraweeView mSdvGoodsImage;
        @Bind(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @Bind(R.id.tv_goods_desc)
        TextView mTvGoodsDesc;
        @Bind(R.id.ll_goods)
        LinearLayout mLlGoods;
        @Bind(R.id.tv_goods_category)
        TextView mTvGoodsCategory;

        ContentViewHolder(View view) {
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

    public void setmOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnRecyclerViewItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Goods goods);
    }

}