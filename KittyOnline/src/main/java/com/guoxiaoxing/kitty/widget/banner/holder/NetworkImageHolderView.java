package com.guoxiaoxing.kitty.widget.banner.holder;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 网络图片
 */
public class NetworkImageHolderView implements Holder<String> {

    private SimpleDraweeView mSimpleDraweeView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
//        View view = LayoutInflater.from(context).inflate(R.layout.shopping_head_banner, null);
//        mSimpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_banner_item);
        mSimpleDraweeView = new SimpleDraweeView(context);
        return mSimpleDraweeView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
//        ImageLoader.getInstance().displayImage(data,imageView);
        mSimpleDraweeView.setImageURI(Uri.parse(data));
    }
}
