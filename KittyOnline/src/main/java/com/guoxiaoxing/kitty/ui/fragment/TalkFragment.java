package com.guoxiaoxing.kitty.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.TalkAdapter;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.UIHelper;

import java.util.List;

import butterknife.Bind;


/**
 * @author guoxiaoxing
 */
public class TalkFragment extends BaseFragment {


    @Bind(R.id.rv_container)
    RecyclerView mRecyclerView;

    private TalkAdapter mTalkAdapter;

    public static TalkFragment newInstance(String info) {
        Bundle args = new Bundle();
        TalkFragment fragment = new TalkFragment();
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
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTalkAdapter = new TalkAdapter();
//        mRecyclerView.setAdapter(new AlphaInAnimationAdapter(mTalkAdapter));
        mRecyclerView.setAdapter(mTalkAdapter);
        mTalkAdapter.setmOnRecyclerVIewItemClickListener(new TalkAdapter.OnRecyclerVIewItemClickListener() {
            @Override
            public void onItemClick(View view, UserTalk userTalk) {
                UIHelper.showTalkDetail(view.getContext(), userTalk, 0);
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
                    mTalkAdapter.notifyDataSetChanged();
                } else {

                }
            }
        });
    }
}
