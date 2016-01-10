package com.guoxiaoxing.kitty.team.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaTeamApi;
import com.guoxiaoxing.kitty.base.BaseListFragment;
import com.guoxiaoxing.kitty.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.bean.SimpleBackPage;
import com.guoxiaoxing.kitty.team.adapter.TeamProjectListAdapterNew;
import com.guoxiaoxing.kitty.team.bean.Team;
import com.guoxiaoxing.kitty.team.bean.TeamProject;
import com.guoxiaoxing.kitty.team.bean.TeamProjectList;
import com.guoxiaoxing.kitty.team.ui.TeamMainActivity;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * TeamProjectFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-2-28 下午4:08:58
 */
public class TeamProjectFragment extends BaseListFragment<TeamProject> {
    
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
    protected TeamProjectListAdapterNew getListAdapter() {
	// TODO Auto-generated method stub
	return new TeamProjectListAdapterNew();
    }
    
    @Override
    protected String getCacheKeyPrefix() {
	return "team_project_list_" + mTeamId + "_" + mCurrentPage;
    }

    @Override
    protected TeamProjectList parseList(InputStream is) throws Exception {
	TeamProjectList list = XmlUtils.toBean(TeamProjectList.class, is);
	return list;
    }

    @Override
    protected TeamProjectList readList(Serializable seri) {
	return ((TeamProjectList) seri);
    }

    @Override
    protected void sendRequestData() {
        // TODO Auto-generated method stub
	OSChinaTeamApi.getTeamProjectList(mTeamId, mHandler);
    }
    
    public TeamProjectFragment() {
	// TODO Auto-generated constructor stub
    }
    
    @Override
    protected void executeOnLoadDataSuccess(List<TeamProject> data) {
        // TODO Auto-generated method stub
        super.executeOnLoadDataSuccess(data);
        if (mAdapter.getData().isEmpty()) {
            setNoProject();
        }
        mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
    }
    
    private void setNoProject() {
	mErrorLayout.setErrorType(EmptyLayout.NODATA);
	mErrorLayout.setErrorImag(R.drawable.page_icon_empty);
	String str = getResources().getString(R.string.team_empty_project);
	mErrorLayout.setErrorMessage(str);
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	TeamProject teamProject = mAdapter.getItem(position);
	if (teamProject != null) {
	    Bundle bundle = new Bundle();
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, mTeam);
	    bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT, teamProject);
	    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_PROJECT_MAIN, bundle);
	}
    }
}
