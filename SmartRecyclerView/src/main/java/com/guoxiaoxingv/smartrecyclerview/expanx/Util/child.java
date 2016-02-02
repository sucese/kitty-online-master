package com.guoxiaoxingv.smartrecyclerview.expanx.Util;

import android.view.View;

import com.guoxiaoxingv.smartrecyclerview.expanx.ExpandableItemData;

/**
 * Created by hesk on 10/7/15.
 */
public abstract class child<T extends ExpandableItemData> extends BaseViewHolder<T> implements ChildVH<T> {
    public child(View itemView) {
        super(itemView);
    }


    protected ChildClickListener listener;

    public void setChildListener(ChildClickListener mlistener) {
        this.listener = mlistener;
    }


}
