package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

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

    @Bind(R.id.ll_active)
    LinearLayout mLlActive;
    @Bind(R.id.ll_find_friend)
    LinearLayout mLlFindFriend;
    @Bind(R.id.ll_activities)
    LinearLayout mLlActivities;
    @Bind(R.id.ll_city)
    LinearLayout mLlCity;
    @Bind(R.id.ll_scan)
    LinearLayout mLlScan;
    @Bind(R.id.ll_shake)
    LinearLayout mLlShake;

    private static BadgeView mMesCount;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_active:
                UIHelper.showMyActive(getActivity());
                break;
            case R.id.ll_find_friend:
                UIHelper.showMyActive(getActivity());
                break;
            case R.id.ll_city:
                UIHelper.showMyActive(getActivity());
                break;
            case R.id.ll_activities:
                UIHelper.showMyActive(getActivity());
                break;
            case R.id.ll_scan:
                UIHelper.showScanActivity(getActivity());
                break;
            case R.id.ll_shake:
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
        mLlActive.setOnClickListener(this);
        mLlFindFriend.setOnClickListener(this);
        mLlCity.setOnClickListener(this);
        mLlActivities.setOnClickListener(this);
        mLlScan.setOnClickListener(this);
        mLlShake.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


}
