package com.guoxiaoxing.kitty.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.bean.Post;
import com.guoxiaoxing.kitty.bean.PostList;
import com.guoxiaoxing.kitty.util.HTMLUtil;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.ThemeSwitchUtils;
import com.guoxiaoxing.kitty.widget.AvatarView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * post（讨论区帖子）适配器
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月9日 下午6:22:54
 */
public class PostAdapter extends ListBaseAdapter<Post> {

    static class ViewHolder {

        @InjectView(R.id.tv_title)
        TextView title;
        @InjectView(R.id.tv_description)
        TextView description;
        @InjectView(R.id.tv_author)
        TextView author;
        @InjectView(R.id.tv_date)
        TextView time;
        @InjectView(R.id.tv_count)
        TextView comment_count;

        @InjectView(R.id.iv_face)
        public AvatarView face;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.list_cell_post, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Post post = (Post) mDatas.get(position);

        vh.face.setUserInfo(post.getAuthorId(), post.getAuthor());
        vh.face.setAvatarUrl(post.getPortrait());
        vh.title.setText(post.getTitle());
        String body = post.getBody();
        vh.description.setVisibility(View.GONE);
        if (null != body || !StringUtils.isEmpty(body)) {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(HTMLUtil.replaceTag(post.getBody()).trim());
        }

        if (AppContext.isOnReadedPostList(PostList.PREF_READED_POST_LIST,
                post.getId() + "")) {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            vh.title.setTextColor(parent.getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        vh.author.setText(post.getAuthor());
        vh.time.setText(StringUtils.friendly_time(post.getPubDate()));
        vh.comment_count.setText(post.getAnswerCount() + "回 / "
                + post.getViewCount() + "阅");

        return convertView;
    }
}
