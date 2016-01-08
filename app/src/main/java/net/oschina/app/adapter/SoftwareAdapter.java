package net.oschina.app.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.bean.SoftwareDec;
import net.oschina.app.bean.SoftwareList;
import net.oschina.app.util.ThemeSwitchUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SoftwareAdapter extends ListBaseAdapter<SoftwareDec> {

    static class ViewHold {
        @InjectView(R.id.tv_title)
        TextView name;
        @InjectView(R.id.tv_software_des)
        TextView des;

        public ViewHold(View view) {
            ButterKnife.inject(this, view);
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
