package com.guoxiaoxing.kitty.ui.welcome.outlayer;

import android.content.Intent;
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
import com.guoxiaoxing.kitty.service.LogUploadService;
import com.guoxiaoxing.kitty.ui.MainActivity;


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

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_zoom_in);
        mSimpleDraweeView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent uploadLog = new Intent(getActivity(), LogUploadService.class);
                getActivity().startService(uploadLog);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
