package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.TalkDetailAdapter;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;

import butterknife.Bind;

/***
 * 说说详情，实际每个item显示的数据类型是Comment
 *
 * @author guoxiaoxing
 */
public class TalkDetailFragment extends BaseFragment {

    @Bind(R.id.rv_talk_detail)
    RecyclerView mRecyclerView;

    private TalkDetailAdapter mTalkDetailAdapter;

    public TalkDetailFragment() {

    }

    public static TalkDetailFragment newInstance() {
        TalkDetailFragment fragment = new TalkDetailFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk_detail_detail;
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        Intent intent = getActivity().getIntent();
        UserTalk userTalk = intent.getParcelableExtra("userTalk");

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTalkDetailAdapter = new TalkDetailAdapter();
        mTalkDetailAdapter.setData(userTalk);
        mRecyclerView.setAdapter(mTalkDetailAdapter);
        mTalkDetailAdapter.notifyDataSetChanged();

    }

    @Override
    public void initData() {
        super.initData();

//        AVQuery<UserTalk> query = AVQuery.getQuery(UserTalk.class);
//        query.findInBackground(new FindCallback<UserTalk>() {
//            @Override
//            public void done(List<UserTalk> list, AVException e) {
//
//                if (e == null) {
//                    mTalkDetailAdapter.setData(list);
//                    mTalkDetailAdapter.notifyDataSetChanged();
//                } else {
//
//                }
//            }
//        });

    }

    //    @Override
//    public boolean onBackPressed() {
//        if (outAty.emojiFragment.isShowEmojiKeyBoard()) {
//            outAty.emojiFragment.hideAllKeyBoard();
//            return true;
//        }
//        if (outAty.emojiFragment.getEditText().getTag() != null) {
//            outAty.emojiFragment.getEditText().setTag(null);
//            outAty.emojiFragment.getEditText().setHint("说点什么吧");
//            return true;
//        }
//        return super.onBackPressed();
//    }
}
