package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.base.BaseFragment;
import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.ui.FindUserActivity;
import com.guoxiaoxing.kitty.ui.ShakeActivity;
import com.guoxiaoxing.kitty.util.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 发现页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月4日 下午3:34:07
 * 
 */

public class ExploreFragment extends BaseFragment {

    @InjectView(R.id.rl_active)
    View mRlActive;

    @InjectView(R.id.rl_find_osc)
    View mFindOSCer;

    @InjectView(R.id.rl_city)
    View mCity;

    @InjectView(R.id.rl_activities)
    View mActivities;

    @InjectView(R.id.rl_scan)
    View mScan;

    @InjectView(R.id.rl_shake)
    View mShake;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_explore, null);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
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
