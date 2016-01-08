package net.oschina.app.team.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaTeamApi;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.team.adapter.TeamProjectMemberAdapter;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamMember;
import net.oschina.app.team.bean.TeamMemberList;
import net.oschina.app.team.bean.TeamProject;
import net.oschina.app.team.ui.TeamMainActivity;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * TeamProjectFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-2-28 下午4:08:58
 */
public class TeamProjectMemberFragment extends
	BaseListFragment<TeamMember> {

    private Team mTeam;

    private int mTeamId;

    private TeamProject mTeamProject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle bundle = getArguments();
	if (bundle != null) {
	    mTeam = (Team) bundle
		    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
	    
	    mTeamProject = (TeamProject) bundle.getSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT);

	    mTeamId = mTeam.getId();
	}
    }

    @Override
    protected TeamProjectMemberAdapter getListAdapter() {
	// TODO Auto-generated method stub
	return new TeamProjectMemberAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
	return "team_project_member_list_" + mTeamId + "_" + mTeamProject.getGit().getId();
    }

    @Override
    protected TeamMemberList parseList(InputStream is) throws Exception {
	TeamMemberList list = XmlUtils.toBean(
		TeamMemberList.class, is);
	return list;
    }

    @Override
    protected TeamMemberList readList(Serializable seri) {
	return ((TeamMemberList) seri);
    }

    @Override
    protected void sendRequestData() {
	// TODO Auto-generated method stub
	OSChinaTeamApi.getTeamProjectMemberList(mTeamId, mTeamProject,
		mHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(List<TeamMember> data) {
	// TODO Auto-generated method stub
	super.executeOnLoadDataSuccess(data);
	if (mAdapter.getData().isEmpty()) {
	    setNoProjectMember();
	}
	mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
    }

    private void setNoProjectMember() {
	mErrorLayout.setErrorType(EmptyLayout.NODATA);
	mErrorLayout.setErrorImag(R.drawable.page_icon_empty);
	String str = getResources().getString(
		R.string.team_empty_project_member);
	mErrorLayout.setErrorMessage(str);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
	TeamMember teamMember = mAdapter.getItem(position);
	if (teamMember != null) {
	    UIHelper.showTeamMemberInfo(getActivity(), mTeamId, teamMember);
	}
    }
}
