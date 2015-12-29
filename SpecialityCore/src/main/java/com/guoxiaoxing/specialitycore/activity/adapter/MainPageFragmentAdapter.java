package com.guoxiaoxing.specialitycore.activity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guoxiaoxing.specialitycore.fragment.CartFragment;
import com.guoxiaoxing.specialitycore.fragment.HomeFragment;
import com.guoxiaoxing.specialitycore.fragment.MineFragment;

/**
 * Created by guoxiaoxing on 15-12-29.
 */
public class MainPageFragmentAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private String[] mTitles = {"首页", "分类", "购物车", "我的"};

    public MainPageFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new CartFragment();
        } else if (position == 2) {
            return new CartFragment();
        } else if (position == 3) {
            return new MineFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
