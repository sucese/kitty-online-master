package com.guoxiaoxing.kitty.ui.fragment;

import java.io.InputStream;
import java.io.Serializable;

import com.guoxiaoxing.kitty.adapter.PostAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.model.Post;
import com.guoxiaoxing.kitty.model.PostList;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.view.View;
import android.widget.AdapterView;

/**
 * 问答
 * 
 * @author william_sim
 */
public class PostsFragment extends BaseListFragment<Post> {

    protected static final String TAG = PostsFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "postslist_";

    @Override
    protected PostAdapter getListAdapter() {
        return new PostAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected PostList parseList(InputStream is) throws Exception {
        PostList list = XmlUtils.toBean(PostList.class, is);
        return list;
    }

    @Override
    protected PostList readList(Serializable seri) {
        return ((PostList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getPostList(mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Post post = mAdapter.getItem(position);
        if (post != null) {
            UIHelper.showPostDetail(view.getContext(), post.getId(),
                    post.getAnswerCount());
            // 放入已读列表
            saveToReadedList(view, PostList.PREF_READED_POST_LIST, post.getId()
                    + "");
        }
    }
}
