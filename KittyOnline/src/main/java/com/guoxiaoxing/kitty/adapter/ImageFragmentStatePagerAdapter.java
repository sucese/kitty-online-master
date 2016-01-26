package com.guoxiaoxing.kitty.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.LoginAnimImageFristFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.LoginAnimImageSecondFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.LoginAnimImageThridFragment;

import java.util.ArrayList;



/**
 * 总共上层有3页动画,写死在adapter
 */
public class ImageFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> mFragments;
    LoginAnimImageFristFragment mLoginAnimImageFristFragment;
    LoginAnimImageSecondFragment mLoginAnimImageSecondFragment;
    LoginAnimImageThridFragment mLoginAnimImageThridFragment;

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
            mLoginAnimImageFristFragment = new LoginAnimImageFristFragment();
            mFragments.add(mLoginAnimImageFristFragment);
            mLoginAnimImageSecondFragment = new LoginAnimImageSecondFragment();
            mFragments.add(mLoginAnimImageSecondFragment);
            mLoginAnimImageThridFragment = new LoginAnimImageThridFragment();
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
