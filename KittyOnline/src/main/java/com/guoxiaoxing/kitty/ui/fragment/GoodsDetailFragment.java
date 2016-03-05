package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.Goods;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.widget.banner.ConvenientBanner;
import com.guoxiaoxing.kitty.widget.banner.holder.CBViewHolderCreator;
import com.guoxiaoxing.kitty.widget.banner.holder.NetworkImageHolderView;
import com.guoxiaoxing.kitty.widget.banner.transforms.FlipHorizontalTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 商品详情Fragment
 *
 * @author guoxiaoxing
 */
public class GoodsDetailFragment extends BaseFragment {


    @Bind(R.id.cb_goods_detail)
    ConvenientBanner mCbGoodsDetail;
    @Bind(R.id.tb_content)
    TabLayout mTbContent;
    @Bind(R.id.vp_title)
    ViewPager mVpTitle;


    private String[] images = {
            "http://7xq26k.com1.z0.glb.clouddn.com/conmon_goods_detail_banner_1.png",
            "http://7xq26k.com1.z0.glb.clouddn.com/conmon_goods_detail_banner_2.png",
            "http://7xq26k.com1.z0.glb.clouddn.com/conmon_goods_detail_banner_3.png",
            "http://7xq26k.com1.z0.glb.clouddn.com/conmon_goods_detail_banner_4.png",
            "http://7xq26k.com1.z0.glb.clouddn.com/conmon_goods_detail_banner_5.png"};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCbGoodsDetail.startTurning(AppConfig.VIEWPAGER_TRANSFORM_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCbGoodsDetail.stopTurning();
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        setupViewPager(mVpTitle);
        mTbContent.addTab(mTbContent.newTab().setText("图文详情"));
        mTbContent.addTab(mTbContent.newTab().setText("商品参数"));
        mTbContent.addTab(mTbContent.newTab().setText("热卖推荐"));
        mTbContent.setupWithViewPager(mVpTitle);

        mCbGoodsDetail.getViewPager().setPageTransformer(true, new FlipHorizontalTransformer());
        mCbGoodsDetail.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, Arrays.asList(images))//设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setOnPageChangeListener(this)//监听翻页事件
//                            .setOnItemClickListener(this);
        ;
    }

    @Override
    public void initData() {
        super.initData();
    }

    private void setupViewPager(ViewPager mVpTitle) {

        Intent intent = getActivity().getIntent();
        Goods goods = intent.getParcelableExtra("goods");

        MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());

        GoodsDisplayFragment fragment1 = GoodsDisplayFragment.newInstance();
        fragment1.setData(goods);
        adapter.addFragment(fragment1, "图文详情");

        GoodsDisplayFragment fragment2 = GoodsDisplayFragment.newInstance();
        fragment2.setData(goods);
        adapter.addFragment(fragment2, "商品参数");

        GoodsDisplayFragment fragment3 = GoodsDisplayFragment.newInstance();
        fragment3.setData(goods);
        adapter.addFragment(fragment3, "热卖推荐");

        mVpTitle.setAdapter(adapter);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {


        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mTitleList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mTitleList.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }


}
