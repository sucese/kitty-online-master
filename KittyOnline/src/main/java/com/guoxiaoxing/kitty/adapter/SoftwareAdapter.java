package com.guoxiaoxing.kitty.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.SoftwareDec;
import com.guoxiaoxing.kitty.model.SoftwareList;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.util.ThemeSwitchUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SoftwareAdapter extends ListBaseAdapter<SoftwareDec> {

    static class ViewHold {
        @Bind(R.id.tv_title)
        TextView name;
        @Bind(R.id.tv_software_des)
        TextView des;

        public ViewHold(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {

        ViewHold vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_software, null);
            vh = new ViewHold(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHold) convertView.getTag();
        }

        SoftwareDec softwareDes = (SoftwareDec) mDatas.get(position);
        vh.name.setText(softwareDes.getName());

        if (AppContext.isOnReadedPostList(SoftwareList.PREF_READED_SOFTWARE_LIST,
                softwareDes.getName())) {
            vh.name.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            vh.name.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        vh.des.setText(softwareDes.getDescription());

        return convertView;
    }
}
