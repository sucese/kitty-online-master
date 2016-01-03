package com.guoxiaoxing.specialitycore.activity;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        CategoryFragment.OnFragmentInteractionListener, CartFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener {

    @Bind(R.id.main_content_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.main_tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mViewPager.setAdapter(new MainPageFragmentAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);


        mTabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mTabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mTabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        mTabLayout.getTabAt(3).setIcon(getResources().getDrawable(R.mipmap.ic_launcher));

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
