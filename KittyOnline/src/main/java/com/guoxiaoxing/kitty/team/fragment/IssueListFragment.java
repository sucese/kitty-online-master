package com.guoxiaoxing.kitty.team.fragment;

import java.io.InputStream;
import java.io.Serializable;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.api.remote.OSChinaTeamApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.ListEntity;
import com.guoxiaoxing.kitty.team.adapter.TeamIssueAdapter;
import com.guoxiaoxing.kitty.team.bean.TeamIssue;
import com.guoxiaoxing.kitty.team.bean.TeamIssueList;
import com.guoxiaoxing.kitty.util.XmlUtils;

/**
 * 任务列表界面
 *
 * @author guoxiaoxing
 *
 */

public class IssueListFragment extends BaseListFragment<TeamIssue> {

    private final String CACHE_KEY_PREFIX = "issue_list_";

    private final int mTeamId = 12481;

    private final int mProjectId = 435;

    @Override
    protected TeamIssueAdapter getListAdapter() {
        return new TeamIssueAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_" + mCurrentPage;
    }

    @Override
    protected TeamIssueList parseList(InputStream is) throws Exception {
        TeamIssueList list = XmlUtils.toBean(TeamIssueList.class, is);
        return list;
    }

    @Override
    protected void sendRequestData() {
        OSChinaTeamApi.getTeamIssueList(mTeamId, mProjectId, 0, "", 253900,
                "state", "scope", mCurrentPage, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected ListEntity<TeamIssue> readList(Serializable seri) {
        return null;
    }
}
