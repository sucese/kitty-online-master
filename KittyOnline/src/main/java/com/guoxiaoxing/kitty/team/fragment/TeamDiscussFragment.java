package com.guoxiaoxing.kitty.team.fragment;

import java.io.InputStream;
import java.io.Serializable;

import com.guoxiaoxing.kitty.api.remote.OSChinaTeamApi;
import com.guoxiaoxing.kitty.ui.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.ListEntity;
import com.guoxiaoxing.kitty.team.adapter.TeamDiscussAdapter;
import com.guoxiaoxing.kitty.team.bean.Team;
import com.guoxiaoxing.kitty.team.bean.TeamDiscuss;
import com.guoxiaoxing.kitty.team.bean.TeamDiscussList;
import com.guoxiaoxing.kitty.team.ui.TeamMainActivity;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * team讨论区列表界面
 *
 * @author guoxiaoxing
 */
public class TeamDiscussFragment extends BaseListFragment<TeamDiscuss> {

    protected static final String TAG = TeamDiscussFragment.class
            .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "team_discuss_list_";

    private Team mTeam;

    private int mTeamId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Team team = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
            if (team != null) {
                mTeam = team;
                mTeamId = StringUtils.toInt(mTeam.getId());
            }
        }
    }

    @Override
    protected TeamDiscussAdapter getListAdapter() {
        return new TeamDiscussAdapter();
    }

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mTeamId + "_" + mCurrentPage;
    }

    @Override
    protected TeamDiscussList parseList(InputStream is)
            throws Exception {
        TeamDiscussList list = XmlUtils.toBean(
                TeamDiscussList.class, is);
        return list;
    }

    @Override
    protected ListEntity<TeamDiscuss> readList(Serializable seri) {
        return ((TeamDiscussList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaTeamApi.getTeamDiscussList("new", mTeamId, 0, mCurrentPage,
                mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
	TeamDiscuss item = (TeamDiscuss) mAdapter.getItem(position);
	if (item != null) {
	    UIHelper.showTeamDiscussDetail(getActivity(), mTeam, item);
	}

    }

}