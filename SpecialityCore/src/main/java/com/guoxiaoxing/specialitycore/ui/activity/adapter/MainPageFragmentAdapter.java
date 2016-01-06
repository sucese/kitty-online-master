package com.guoxiaoxing.specialitycore.ui.activity.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.guoxiaoxing.specialitycore.ui.fragment.CartFragment;
import com.guoxiaoxing.specialitycore.ui.fragment.CategoryFragment;
import com.guoxiaoxing.specialitycore.ui.fragment.HomeFragment;
import com.guoxiaoxing.specialitycore.ui.fragment.MineFragment;

/**
 * Created by guoxiaoxing on 15-12-29.
 */
public class MainPageFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private FragmentManager mFragmentManager;

    public MainPageFragmentAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        mFragmentManager = fm;
        mContext = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new CategoryFragment();
        } else if (position == 2) {
            return new CartFragment();
        } else if (position == 3) {
            return new MineFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }


}
