package com.guoxiaoxing.kitty.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.FristImageFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.SecondImageFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.ThridImageFragment;

import java.util.ArrayList;



/**
 * 总共上层有3页动画,写死在adapter
 */
public class ImageFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments;
    FristImageFragment mLoginAnimImageFristFragment;
    SecondImageFragment mLoginAnimImageSecondFragment;
    ThridImageFragment mLoginAnimImageThridFragment;

    public Fragment getFragement(int position) {
        switch (position) {
            case 0:
                return mLoginAnimImageFristFragment;
            case 1:
                return mLoginAnimImageSecondFragment;
            case 2:
                return mLoginAnimImageThridFragment;
        }
        return null;
    }

    public ImageFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        if(mFragments == null){
            mFragments = new ArrayList<Fragment>();
            mLoginAnimImageFristFragment = new FristImageFragment();
            mFragments.add(mLoginAnimImageFristFragment);
            mLoginAnimImageSecondFragment = new SecondImageFragment();
            mFragments.add(mLoginAnimImageSecondFragment);
            mLoginAnimImageThridFragment = new ThridImageFragment();
            mFragments.add(mLoginAnimImageThridFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mLoginAnimImageFristFragment;
            case 1:
                return mLoginAnimImageSecondFragment;
            case 2:
                return mLoginAnimImageThridFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
