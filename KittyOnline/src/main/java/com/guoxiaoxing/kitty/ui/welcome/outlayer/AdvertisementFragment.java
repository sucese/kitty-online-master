package com.guoxiaoxing.kitty.ui.welcome.outlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guoxiaoxing.kitty.R;


/**
 * 登录层
 */
public class AdvertisementFragment extends Fragment {

    SimpleDraweeView mSimpleDraweeView;

    FrameLayout mFlParent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_welcomanim_login, null);
        mFlParent = (FrameLayout) view.findViewById(R.id.fl_parent);
        mSimpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.app_start_ad);
        return view;
    }

    public void playInAnim() {

        Animation hyperspaceJump = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_zoom_in);
        mSimpleDraweeView.startAnimation(hyperspaceJump);
    }
}
