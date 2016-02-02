package com.guoxiaoxingv.smartrecyclerview.quickAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.guoxiaoxingv.smartrecyclerview.AdmobAdapter;
import com.guoxiaoxingv.smartrecyclerview.SmartRecyclerviewViewHolder;

import java.util.List;

/**
 * Created by hesk on 4/8/15.
 */
public abstract class simpleAdmobAdapter<T, B extends SmartRecyclerviewViewHolder, GoogleAdView extends ViewGroup> extends AdmobAdapter<GoogleAdView, T, B> {
    public simpleAdmobAdapter(GoogleAdView adview, boolean insertOnce, int setInterval, List<T> L) {
        super(adview, insertOnce, setInterval, L);
    }

    public simpleAdmobAdapter(GoogleAdView adview, boolean insertOnce, int setInterval, List<T> L, AdviewListener listener) {
        super(adview, insertOnce, setInterval, L, listener);
    }

    @Override
    public SmartRecyclerviewViewHolder getViewHolder(View view) {
        return new SmartRecyclerviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmartRecyclerviewViewHolder holder, int position) {
        if (onActionToBindData(position, list)) {
            this.withBindHolder((B) holder, this.list.get(position), position);
        }
    }

    protected abstract void withBindHolder(B var1, T var2, int var3);


    @Override
    public int getAdapterItemCount() {
        return super.getAdapterItemCount();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }


}
