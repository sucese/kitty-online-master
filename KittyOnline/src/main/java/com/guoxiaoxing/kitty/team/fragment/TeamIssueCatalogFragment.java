package com.guoxiaoxing.kitty.team.fragment;

import java.io.InputStream;
import java.io.Serializable;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.api.remote.OSChinaTeamApi;
import com.guoxiaoxing.kitty.base.BaseListFragment;
import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.team.adapter.TeamIssueCatalogAdapter;
import com.guoxiaoxing.kitty.team.bean.Team;
import com.guoxiaoxing.kitty.team.bean.TeamIssueCatalog;
import com.guoxiaoxing.kitty.team.bean.TeamIssueCatalogList;
import com.guoxiaoxing.kitty.team.bean.TeamProject;
import com.guoxiaoxing.kitty.team.ui.TeamMainActivity;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 任务分组列表
 * 
 * TeamIssueCatalogFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-1 下午3:36:03
 */
public class TeamIssueCatalogFragment extends
	BaseListFragment<TeamIssueCatalog> {
    
    private Team mTeam;
    
    private TeamProject mTeamProject;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTeam = (Team) args.getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
            mTeamProject = (TeamProject) args.getSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT);
        }
    }

    @Override
    protected TeamIssueCatalogAdapter getListAdapter() {
	// TODO Auto-generated method stub
	return new TeamIssueCatalogAdapter();
    }
    
    @Override
    protected TeamIssueCatalogList parseList(InputStream is)
            throws Exception {
        return XmlUtils.toBean(TeamIssueCatalogList.class, is);
    }
    
    @Override
    protected TeamIssueCatalogList readList(Serializable seri) {
        return (TeamIssueCatalogList)seri;
    }

    @Override
    protected void sendRequestData() {
	int uid = AppContext.getInstance().getLoginUid();
	int teamId= mTeam.getId();
	int projectId = mTeamProject.getGit().getId();
	String source = mTeamProject.getSource();
	OSChinaTeamApi.getTeamCatalogIssueList(uid, teamId, projectId, source, mHandler);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	TeamIssueCatalog teamIssueCatalog = mAdapter.getItem(position);
	if (teamIssueCatalog != null) {
	    Bundle bundle = new Bundle();
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, mTeam);
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT, mTeamProject);
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_ISSUE_CATALOG, teamIssueCatalog);
	    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_ISSUECATALOG_ISSUE_LIST, bundle);
	}
    }
    
    @Override
    protected String getCacheKeyPrefix() {
        // TODO Auto-generated method stub
        return "team_issue_catalog_list" + mTeam.getId() + "_" + mTeamProject.getGit().getId();
    }
}

