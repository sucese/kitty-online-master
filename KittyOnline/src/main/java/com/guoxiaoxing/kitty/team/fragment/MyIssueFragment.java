package com.guoxiaoxing.kitty.team.fragment;

import java.io.InputStream;
import java.io.Serializable;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.team.adapter.TeamIssueAdapter;
import com.guoxiaoxing.kitty.team.bean.Team;
import com.guoxiaoxing.kitty.team.bean.TeamIssue;
import com.guoxiaoxing.kitty.team.bean.TeamIssueList;
import com.guoxiaoxing.kitty.team.ui.TeamMainActivity;
import com.guoxiaoxing.kitty.team.viewpagefragment.MyIssuePagerfragment;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 我的任务列表界面
 * 
 * @author guoxiaoxing
 */
public class MyIssueFragment extends BaseListFragment<TeamIssue> {

    protected static final String TAG = TeamIssueFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "my_issue_";

    private Team mTeam;
    private String type = "all";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeam = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
            type = bundle.getString(MyIssuePagerfragment.MY_ISSUEDETAIL_KEY,
                    "all");
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mListView.setDivider(new ColorDrawable(0x00000000));
        mListView.setSelector(new ColorDrawable(0x00000000));
    }

    @Override
    protected TeamIssueAdapter getListAdapter() {
        return new TeamIssueAdapter();
    }

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + AppContext.getInstance().getLoginUid() + "_"
                + mTeam.getId() + mCurrentPage + type;
    }

    @Override
    protected TeamIssueList parseList(InputStream is) throws Exception {
        TeamIssueList list = XmlUtils.toBean(TeamIssueList.class, is);
        return list;
    }

    @Override
    protected TeamIssueList readList(Serializable seri) {
        return ((TeamIssueList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getMyIssue(mTeam.getId() + "", AppContext.getInstance()
                .getLoginUid() + "", mCurrentPage, type, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        TeamIssue issue = mAdapter.getItem(position);
        if (issue != null) {
            UIHelper.showTeamIssueDetail(getActivity(), mTeam, issue, null);
        }
    }
}
