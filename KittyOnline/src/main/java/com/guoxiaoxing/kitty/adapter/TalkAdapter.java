package com.guoxiaoxing.kitty.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.ViewHolder> implements View.OnClickListener {


    private List<UserTalk> mTalkList = new ArrayList<>();
    private OnRecyclerVIewItemClickListener mOnRecyclerVIewItemClickListener = null;

    public TalkAdapter() {

    }

    public void setData(List<UserTalk> talkList) {
        mTalkList.addAll(talkList);
        mTalkList.addAll(talkList);
        mTalkList.addAll(talkList);
        mTalkList.addAll(talkList);
        mTalkList.addAll(talkList);
    }

    private List<UserTalk> getData() {
        return mTalkList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_talk, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        List<String> list = mTalkList.get(position).getImageUrlList();
        holder.mNgvTalkImage.setImagesData(new ArrayList<>(list));
        holder.mTvUserName.setText(mTalkList.get(position).getUserName());
        holder.mTvReleaseTime.setText(mTalkList.get(position).getTalkTime());
        holder.mTvTalkContent.setText(mTalkList.get(position).getTalkContent());
        holder.mTvFirstComment.setText(mTalkList.get(position).getCommentList().get(0));
        holder.mTvSecondComment.setText(mTalkList.get(position).getCommentList().get(1));
        holder.mTvLike.setText(String.valueOf(position * 10 + 5));
        holder.mTvComment.setText(String.valueOf(position * 10 + 8));
        holder.itemView.setTag(mTalkList.get(position));

    }

    @Override
    public int getItemCount() {
        return mTalkList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerVIewItemClickListener != null) {
            mOnRecyclerVIewItemClickListener.onItemClick(v, (UserTalk) v.getTag());
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.sdv_user_logo)
        SimpleDraweeView mSdvUserLogo;
        @Bind(R.id.tv_user_name)
        TextView mTvUserName;
        @Bind(R.id.tv_release_time)
        TextView mTvReleaseTime;
        @Bind(R.id.tv_attention)
        TextView mTvAttention;
        @Bind(R.id.tv_talk_content)
        TextView mTvTalkContent;
        @Bind(R.id.ngv_talk_image)
        NineGridlayout mNgvTalkImage;
        @Bind(R.id.ib_like)
        ImageButton mTbLike;
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mTbLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public void setmOnRecyclerVIewItemClickListener(OnRecyclerVIewItemClickListener listener) {
        mOnRecyclerVIewItemClickListener = listener;
    }

    public interface OnRecyclerVIewItemClickListener {

        void onItemClick(View view, UserTalk userTalk);

    }
}