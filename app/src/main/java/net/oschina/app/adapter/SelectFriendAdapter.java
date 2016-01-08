package net.oschina.app.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.ui.SelectFriendsActivity;
import net.oschina.app.widget.AvatarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * <p>Created 15/8/26 下午2:09.</p>
 * <p><a href="mailto:730395591@qq.com">Email:730395591@qq.com</a></p>
 * <p><a href="http://www.happycodeboy.com">LeonLee Blog</a></p>
 *
 * @author 李文龙(LeonLee)
 */
public class SelectFriendAdapter extends BaseAdapter {

    private static final char DEFAULT_OTHER_LETTER = '#';

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_NORMAL = 1;

    private List<ItemData> mList = new ArrayList<>();
    //记录索引值对应的位置
    private SparseArray<Integer> mPositionArray = new SparseArray<>();

    private LayoutInflater mInflater;

    public SelectFriendAdapter() {

    }

    protected LayoutInflater getLayoutInflater(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        return mInflater;
    }

    public void setFriendItems(List<SelectFriendsActivity.FriendItem> list) {
        mPositionArray.clear();
        mList.clear();

        //非字母开头的列表
        List<ItemData> otherList = null;

        char lastIndex = '0';
        for(SelectFriendsActivity.FriendItem item : list) {
            char indexLetter;
            char c = item.getFirstLetter();
            if(c >= 'A' && c <= 'Z') {
                indexLetter = c;
            } else if(c >= 'a' && c <= 'z') {
                indexLetter = (char)(c - 'a' + 'A');
            } else {
                indexLetter = DEFAULT_OTHER_LETTER;
            }
            if(indexLetter == DEFAULT_OTHER_LETTER) {
                if(otherList == null) {
                    otherList = new ArrayList<>();
                }
                otherList.add(new NormalItemData(item));
            } else {
                if (indexLetter != lastIndex) {
                    mPositionArray.append(indexLetter, mList.size());
                    mList.add(new HeaderItemData(String.valueOf(indexLetter)));
                    lastIndex = indexLetter;
                }
                mList.add(new NormalItemData(item));
            }
        }
        //将没有索引的数据列表放到最后
        if(otherList != null && !otherList.isEmpty()) {
            mPositionArray.append(DEFAULT_OTHER_LETTER, mList.size());
            mList.add(new HeaderItemData(String.valueOf(DEFAULT_OTHER_LETTER)));
            mList.addAll(otherList);
        }

        notifyDataSetChanged();
    }

    /** 根据索引获取位置*/
    public int getPositionByIndex(char indexLetter) {
        Integer value = mPositionArray.get(indexLetter);
        if(value == null) {
            return -1;
        }
        return value;
    }

    public SelectFriendsActivity.FriendItem getFriendItem(int position) {
        ItemData data = getItem(position);
        if(data instanceof NormalItemData) {
            return ((NormalItemData)data).friendItem;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ItemData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (getItem(position) instanceof HeaderItemData) ? VIEWTYPE_HEADER : VIEWTYPE_NORMAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemData itemData = getItem(position);
        if(itemData instanceof HeaderItemData) {
            final HeaderViewHolder holder;
            if(convertView == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_index_header, parent, false);
                holder = new HeaderViewHolder(convertView);
                convertView.setTag(holder);
                convertView.setEnabled(false);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            holder.bind(((HeaderItemData)itemData).index);
        } else {
            final NormalViewHolder holder;
            if(convertView == null) {
                convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_select_friend, parent, false);
                holder = new NormalViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (NormalViewHolder) convertView.getTag();
            }
            holder.bind(((NormalItemData)itemData).friendItem);
        }

        return convertView;
    }

    private interface ItemData {}

    static class HeaderItemData implements ItemData {
        String index;
        public HeaderItemData(String index) {
            this.index = index;
        }
    }

    static class NormalItemData implements ItemData {
        SelectFriendsActivity.FriendItem friendItem;

        public NormalItemData(SelectFriendsActivity.FriendItem friendItem) {
            this.friendItem = friendItem;
        }
    }

    static class HeaderViewHolder {
        @InjectView(R.id.header_text)
        TextView text;

        HeaderViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bind(String index) {
            text.setText(index);
        }
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

        public void bind(SelectFriendsActivity.FriendItem item) {
            name.setText(item.getFriend().getName());
            avatar.setAvatarUrl(item.getFriend().getPortrait());
            checkBox.setChecked(item.isSelected());

            avatar.setDisplayCircle(false);

        }
    }
}
