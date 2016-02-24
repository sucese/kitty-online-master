package com.guoxiaoxing.kitty.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.TalkAdapter;
import com.guoxiaoxing.kitty.tmp.SampleDataboxset;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * Created by Chenyc on 2015/6/29.
 */
public class DetailFragment extends BaseFragment {


    @Bind(R.id.urv_container)
    UltimateRecyclerView mUltimateRecyclerView;

    private TalkAdapter mTalkAdapter;

    public static DetailFragment newInstance(String info) {
        Bundle args = new Bundle();
        DetailFragment fragment = new DetailFragment();
        args.putString("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mUltimateRecyclerView.setHasFixedSize(true);
        final List<String> stringList = new ArrayList<>();

        stringList.add("111");
        stringList.add("aaa");
        stringList.add("222");
        stringList.add("33");
        stringList.add("44");
        stringList.add("55");
        stringList.add("66");
        stringList.add("11771");


        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTalkAdapter = new TalkAdapter(stringList);
        mUltimateRecyclerView.setAdapter(mTalkAdapter);


//        mUltimateRecyclerView.setItemAnimator(new FadeInAnimator());
//        mUltimateRecyclerView.getItemAnimator().setAddDuration(300);
//        mUltimateRecyclerView.getItemAnimator().setRemoveDuration(300);

        //加载更多
        mUltimateRecyclerView.enableLoadmore();
        mTalkAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_bottom_progressbar, null, false));

        //上拉加载更多
        mUltimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        SampleDataboxset.insertTalk(mTalkAdapter, 10);
                        //  mLayoutManager.scrollToPositionWithOffset(maxLastVisiblePosition, -1);
                        //  mLayoutManager.scrollToPosition(maxLastVisiblePosition);
                    }
                }, 2500);
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
    }
}
