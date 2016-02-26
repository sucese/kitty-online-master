package com.guoxiaoxing.kitty.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.widget.ninelayout.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户说说Adapter
 *
 * @author guoxiaoxing
 */
public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.ViewHolder> {


    private List<UserTalk> mTalkList = new ArrayList<>();

    public TalkAdapter() {

    }

    public void setData(List<UserTalk> talkList) {
        mTalkList = talkList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_talk, parent, false);
        return new ViewHolder(view, true);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        List<String> list = mTalkList.get(position).getImageUrlList();
        holder.mNgvTalkImage.setImagesData(new ArrayList<>(list));
        holder.mTvUserName.setText(mTalkList.get(position).getUserName());
        holder.mTvReleaseTime.setText(mTalkList.get(position).getTalkTime());
        holder.mTvTalkContent.setText(mTalkList.get(position).getTalkContent());

    }

    @Override
    public int getItemCount() {
        return mTalkList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ll_root_talk)
        LinearLayout mLlRoot;
        @Bind(R.id.sdv_user_logo)
        SimpleDraweeView mSdvUserLogo;
        @Bind(R.id.tv_user_name)
        TextView mTvUserName;
        @Bind(R.id.tv_release_time)
        TextView mTvReleaseTime;
        @Bind(R.id.tv_attention)
        TextView mTvAttention;
        @Bind(R.id.ll_user)
        RelativeLayout mLlUser;
        @Bind(R.id.tv_talk_content)
        TextView mTvTalkContent;
        @Bind(R.id.ngv_talk_image)
        NineGridlayout mNgvTalkImage;
        @Bind(R.id.tv_like)
        TextView mTvLike;
        @Bind(R.id.tv_comment)
        TextView mTvComment;
        @Bind(R.id.tv_first_comment)
        TextView mTvFirstComment;
        @Bind(R.id.tv_second_comment)
        TextView mTvSecondComment;
        @Bind(R.id.tv_more_comment)
        TextView mTvMoreComment;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (isItem) {

            }
        }
    }
}