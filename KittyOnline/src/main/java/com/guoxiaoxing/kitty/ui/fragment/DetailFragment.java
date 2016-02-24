package com.guoxiaoxing.kitty.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.TalkAdapter;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.tmp.SampleDataboxset;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

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

        mUltimateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTalkAdapter = new TalkAdapter();
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

        AVQuery<UserTalk> query = AVQuery.getQuery(UserTalk.class);
        query.findInBackground(new FindCallback<UserTalk>() {
            @Override
            public void done(List<UserTalk> list, AVException e) {

                if (e == null) {
                    mTalkAdapter.setData(list);
                } else {

                }
            }
        });
    }
}
