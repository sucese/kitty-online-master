package com.guoxiaoxingv.smartrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guoxiaoxingv.smartrecyclerview.swipe.SwipeItemManagerImpl;
import com.guoxiaoxingv.smartrecyclerview.swipe.SwipeItemManagerInterface;
import com.guoxiaoxingv.smartrecyclerview.swipe.SwipeLayout;

import java.util.List;

/**
 * An abstract adapter which can be extended for Recyclerview
 */
public abstract class SwipeableUltimateViewAdapter extends UltimateViewAdapter<UltimateRecyclerviewViewHolder> implements SwipeItemManagerInterface {

    protected SwipeItemManagerImpl mItemManger = new SwipeItemManagerImpl(this);

    /**
     * Don't forget to call super.onBindViewHolder when overriding
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder holder, int position) {
        mItemManger.updateConvertView(holder, position);
    }

    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public SwipeItemManagerImpl.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(SwipeItemManagerImpl.Mode mode) {
        mItemManger.setMode(mode);
    }

    public static class BaseSwipeableViewHolder extends RecyclerView.ViewHolder {

        public SwipeLayout               swipeLayout      = null;
        public SwipeLayout.OnLayout      onLayoutListener = null;
        public SwipeLayout.SwipeListener swipeMemory      = null;
        public int                       position         = -1;

        public BaseSwipeableViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.recyclerview_swipe);
        }
    }


}
