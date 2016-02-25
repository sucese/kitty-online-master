package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.SimpleBackPage;
import com.guoxiaoxing.kitty.ui.activity.FindUserActivity;
import com.guoxiaoxing.kitty.ui.activity.ShakeActivity;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.widget.BadgeView;

import butterknife.Bind;

/**
 * 发现页面
 *
 * @author guoxiaoxing
 */

public class MainChatFragment extends BaseFragment {


    @Bind(R.id.tb_main_chat_fragment)
    Toolbar mToolbar;

    @Bind(R.id.cv_active)
    CardView mCvActive;
    @Bind(R.id.cv_find_friend)
    CardView mCvFindFriend;
    @Bind(R.id.cv_activities)
    CardView mCvActivities;
    @Bind(R.id.cv_city)
    CardView mCvCity;
    @Bind(R.id.cv_scan)
    CardView mCvScan;
    @Bind(R.id.cv_shake)
    CardView mCvShake;

    private static BadgeView mMesCount;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.cv_active:
                UIHelper.showMyActive(getActivity());
                break;
            case R.id.cv_find_friend:
                showFindUser();
                break;
            case R.id.cv_city:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SAME_CITY);
                break;
            case R.id.cv_activities:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.EVENT_LIST);
                break;
            case R.id.cv_scan:
                UIHelper.showScanActivity(getActivity());
                break;
            case R.id.cv_shake:
                showShake();
                break;
            default:
                break;
        }
    }

    private void showShake() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ShakeActivity.class);
        getActivity().startActivity(intent);
    }

    private void showFindUser() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), FindUserActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void initView(View view) {
        mCvActive.setOnClickListener(this);
        mCvFindFriend.setOnClickListener(this);
        mCvCity.setOnClickListener(this);
        mCvActivities.setOnClickListener(this);
        mCvScan.setOnClickListener(this);
        mCvShake.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


}
