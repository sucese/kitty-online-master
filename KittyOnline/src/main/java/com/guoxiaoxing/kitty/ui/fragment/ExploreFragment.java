package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.ui.activity.FindUserActivity;
import com.guoxiaoxing.kitty.ui.activity.ShakeActivity;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.UIHelper;

import butterknife.Bind;

/**
 * 发现页面
 * 
 * @author guoxiaoxing
 */

public class ExploreFragment extends BaseFragment {

    @Bind(R.id.rl_active)
    View mRlActive;

    @Bind(R.id.rl_find_osc)
    View mFindOSCer;

    @Bind(R.id.rl_city)
    View mCity;

    @Bind(R.id.rl_activities)
    View mActivities;

    @Bind(R.id.rl_scan)
    View mScan;

    @Bind(R.id.rl_shake)
    View mShake;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.rl_active:
            UIHelper.showMyActive(getActivity());
            break;
        case R.id.rl_find_osc:
            showFindUser();
            break;
        case R.id.rl_city:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SAME_CITY);
            break;
        case R.id.rl_activities:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.EVENT_LIST);
            break;
        case R.id.rl_scan:
            UIHelper.showScanActivity(getActivity());
            break;
        case R.id.rl_shake:
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
        mRlActive.setOnClickListener(this);

        mFindOSCer.setOnClickListener(this);
        mCity.setOnClickListener(this);
        mActivities.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mShake.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }
}
