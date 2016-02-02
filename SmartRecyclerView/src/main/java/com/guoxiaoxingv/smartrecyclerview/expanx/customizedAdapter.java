package com.guoxiaoxingv.smartrecyclerview.expanx;

import android.content.Context;

import com.guoxiaoxingv.smartrecyclerview.expanx.Util.child;
import com.guoxiaoxingv.smartrecyclerview.expanx.Util.parent;

/**
 * Created by hesk on 14/7/15.
 */
public abstract class customizedAdapter<G extends parent<SmartItem>, T extends child<SmartItem>> extends LinearExpanxURVAdapter<SmartItem, G, T> {

    public customizedAdapter(Context context) {
        super(context, EXPANDABLE_ITEMS, true);
    }

}
