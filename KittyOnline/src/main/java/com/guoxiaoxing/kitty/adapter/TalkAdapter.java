package com.guoxiaoxing.kitty.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.UserTalk;
import com.guoxiaoxing.kitty.widget.ninelayout.NineGridlayout;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户说说Adapter
 *
 * @author guoxiaoxing
 */
public class TalkAdapter extends UltimateViewAdapter<TalkAdapter.ViewHolder> {


    private List<UserTalk> mTalkList = new ArrayList<>();

    public TalkAdapter() {
    }

    public void setData(List<UserTalk> talkList) {
        mTalkList = talkList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mTalkList.size() : position < mTalkList.size()) && (customHeaderView != null ? position > 0 : true)) {

//            ((ViewHolder) holder).textViewSample.setText(mTalkList.get(customHeaderView != null ? position - 1 : position));
            // ((ViewHolder) holder).itemView.setActivated(selectedItems.get(position, false));
            if (mDragStartListener != null) {
//                ((ViewHolder) holder).imageViewSample.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
//                            mDragStartListener.onStartDrag(holder);
//                        }
//                        return false;
//                    }
//                });

                ((ViewHolder) holder).item_view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        }

    }

    @Override
    public int getAdapterItemCount() {
        return mTalkList.size();
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view, false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_talk, parent, false);
        ViewHolder vh = new ViewHolder(view, true);
        return vh;
    }

    public void insert(UserTalk userTalk, int position) {
        insertInternal(mTalkList, userTalk, position);
    }

    public void remove(int position) {
        removeInternal(mTalkList, position);
    }

    public void clear() {
        clearInternal(mTalkList);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    public void swapPositions(int from, int to) {
        swapPositions(mTalkList, from, to);
    }


    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

//        TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
//        textView.setText(String.valueOf(getItem(position).charAt(0)));
//        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AA70DB93"));
        viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.stick_img);

        SecureRandom imgGen = new SecureRandom();
        switch (imgGen.nextInt(3)) {
            case 0:
                imageView.setImageResource(R.drawable.cat);
                break;
            case 1:
                imageView.setImageResource(R.drawable.cat2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.cactus);
                break;
        }

    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }


    public class ViewHolder extends UltimateRecyclerviewViewHolder {

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
        @Bind(R.id.progressbar)
        ProgressBar progressBarSample;
        View item_view;

        public ViewHolder(View itemView, boolean isItem) {
            super(itemView);
            ButterKnife.bind(itemView);
//            itemView.setOnTouchListener(new SwipeDismissTouchListener(itemView, null, new SwipeDismissTouchListener.DismissCallbacks() {
//                @Override
//                public boolean canDismiss(Object token) {
//                    Logs.d("can dismiss");
//                    return true;
//                }
//
//                @Override
//                public void onDismiss(View view, Object token) {
//                   // Logs.d("dismiss");
//                    remove(getPosition());
//
//                }
//            }));
            if (isItem) {
//                textViewSample = (TextView) itemView.findViewById(
//                        R.id.textview);
//                imageViewSample = (ImageView) itemView.findViewById(R.id.imageview);
                progressBarSample = (ProgressBar) itemView.findViewById(R.id.progressbar);
                progressBarSample.setVisibility(View.GONE);
                item_view = itemView.findViewById(R.id.itemview);
            }

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public UserTalk getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mTalkList.size())
            return mTalkList.get(position);
        else return null;
    }

}