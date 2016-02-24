package com.guoxiaoxing.kitty.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.guoxiaoxing.kitty.R;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.security.SecureRandom;
import java.util.List;

/**
 * 用户说说Adapter
 *
 * @author guoxiaoxing
 */


public class TalkAdapter extends UltimateViewAdapter<TalkAdapter.SimpleAdapterViewHolder> {

    private List<String> mTalkList;

    public TalkAdapter(List<String> mTalkList) {
        this.mTalkList = mTalkList;
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= mTalkList.size() : position < mTalkList.size()) && (customHeaderView != null ? position > 0 : true)) {

//            ((SimpleAdapterViewHolder) holder).textViewSample.setText(mTalkList.get(customHeaderView != null ? position - 1 : position));
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

                ((SimpleAdapterViewHolder) holder).item_view.setOnTouchListener(new View.OnTouchListener() {
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
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_talk, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public void setData() {

    }


    public void insert(String string, int position) {
        insertInternal(mTalkList, string, position);
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
        // URLogs.d("position--" + position + "   " + getItem(position));
        if (getItem(position).length() > 0)
            return getItem(position).charAt(0);
        else return -1;
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

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        swapPositions(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
        super.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        if (position > 0)
            remove(position);
        // notifyItemRemoved(position);
//        notifyDataSetChanged();
        super.onItemDismiss(position);
    }
//
//    private int getRandomColor() {
//        SecureRandom rgen = new SecureRandom();
//        return Color.HSVToColor(150, new float[]{
//                rgen.nextInt(359), 1, 1
//        });
//    }

    public void setOnDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;

    }


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {

//        TextView textViewSample;
//        ImageView imageViewSample;
        ProgressBar progressBarSample;
        View item_view;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
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

    public String getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < mTalkList.size())
            return mTalkList.get(position);
        else return "";
    }

}