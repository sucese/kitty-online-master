package net.oschina.app.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.ui.SelectFriendsActivity;
import net.oschina.app.widget.AvatarView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * <p>Created 15/8/27 下午9:29.</p>
 * <p><a href="mailto:730395591@qq.com">Email:730395591@qq.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author 李文龙(LeonLee
 *
 * 搜索好友结果适配器
 */
public class SearchFriendAdapter extends BaseAdapter {

    final List<SelectFriendsActivity.SearchItem> mSearchResults;
    private LayoutInflater mInflater;

    public SearchFriendAdapter(List<SelectFriendsActivity.SearchItem> list) {
        this.mSearchResults = list;
    }

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    @Override
    public int getCount() {
        return mSearchResults.size();
    }

    @Override
    public SelectFriendsActivity.SearchItem getItem(int position) {
        return mSearchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NormalViewHolder holder;
        if(convertView == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_select_friend, parent, false);
            holder = new NormalViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (NormalViewHolder) convertView.getTag();
        }
        holder.bind(getItem(position));
        return convertView;
    }

    static class NormalViewHolder {
        @InjectView(R.id.tv_name)
        TextView name;
        @InjectView(R.id.iv_avatar)
        AvatarView avatar;
        @InjectView(R.id.cb_check)
        CheckBox checkBox;

        NormalViewHolder(View view) {
            ButterKnife.inject(this, view);
            avatar.setClickable(false);
        }

        public void bind(SelectFriendsActivity.SearchItem item) {
            SelectFriendsActivity.FriendItem friendItem = item.getFriendItem();
            avatar.setAvatarUrl(friendItem.getFriend().getPortrait());
            checkBox.setChecked(friendItem.isSelected());

            avatar.setDisplayCircle(false);

            int start = item.getStartIndex();
            if(start != -1) {
                SpannableString ss = new SpannableString(friendItem.getFriend().getName());
                ss.setSpan(new ForegroundColorSpan(item.getHightLightColor()), start,
                        start + item.getKeyLength(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                name.setText(ss);
            } else {
                name.setText(friendItem.getFriend().getName());
            }

        }
    }

}
