package com.guoxiaoxing.specialitycore.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.guoxiaoxing.specialitycore.R;
import com.guoxiaoxing.specialitycore.activity.adapter.MainPageFragmentAdapter;
import com.guoxiaoxing.specialitycore.fragment.CartFragment;
import com.guoxiaoxing.specialitycore.fragment.CategoryFragment;
import com.guoxiaoxing.specialitycore.fragment.HomeFragment;
import com.guoxiaoxing.specialitycore.fragment.MineFragment;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        CategoryFragment.OnFragmentInteractionListener, CartFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener {

    private Context mContext;
    @Bind(R.id.main_content_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.main_tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //科大讯飞初始化,请勿在“=”与 appid 之间添加任务空字符或者转义符
        SpeechUtility.createUtility(MainActivity.this, SpeechConstant.APPID + "=568b60b0");

        mContext = this;
        ButterKnife.bind(this);

        mViewPager.setAdapter(new MainPageFragmentAdapter(getSupportFragmentManager(), mContext));
        mTabLayout.setupWithViewPager(mViewPager);


        mTabLayout.getTabAt(0).setIcon(R.drawable.main_tab_home_page).setText("首页");
        mTabLayout.getTabAt(1).setIcon(R.drawable.maini_tab_goods_category).setText("分类");
        mTabLayout.getTabAt(2).setIcon(R.drawable.main_tab_shopping_cart).setText("购物车");
        mTabLayout.getTabAt(3).setIcon(R.drawable.main_tab_mine_info).setText("我的");

        mTabLayout.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab)

                    {
                        if (tab == mTabLayout.getTabAt(0)) {
                            mViewPager.setCurrentItem(0);
                        } else if (tab == mTabLayout.getTabAt(1)) {
                            mViewPager.setCurrentItem(1);
                        } else if (tab == mTabLayout.getTabAt(2)) {
                            mViewPager.setCurrentItem(2);
                        } else if (tab == mTabLayout.getTabAt(3)) {
                            mViewPager.setCurrentItem(3);
                        }

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );

    }


    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCartFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCategoryFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMineFragmentInteraction(Uri uri) {

    }
}
