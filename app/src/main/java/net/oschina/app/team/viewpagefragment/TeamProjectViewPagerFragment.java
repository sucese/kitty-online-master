package net.oschina.app.team.viewpagefragment;

import net.oschina.app.R;
import net.oschina.app.adapter.ViewPageFragmentAdapter;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.base.BaseViewPagerFragment;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamProject;
import net.oschina.app.team.fragment.TeamIssueCatalogFragment;
import net.oschina.app.team.fragment.TeamProjectActiveFragment;
import net.oschina.app.team.fragment.TeamProjectMemberFragment;
import net.oschina.app.team.ui.TeamMainActivity;
import net.oschina.app.util.UIHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * TeamProjectViewPagerFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-1 下午2:23:32
 */
public class TeamProjectViewPagerFragment extends BaseViewPagerFragment {
    
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
            ((BaseActivity)getActivity()).setActionBarTitle(mTeamProject.getGit().getName());
        }
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.team_project_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
	int menuId = item.getItemId();
	switch (menuId) {
	case R.id.team_new_issue:
	    UIHelper.showCreateNewIssue(getActivity(), mTeam, mTeamProject, null);
	    break;

	default:
	    break;
	}
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
	adapter.addTab("任务分组", "issue", TeamIssueCatalogFragment.class, getBundle());
	adapter.addTab("动态", "active", TeamProjectActiveFragment.class, getBundle());
	adapter.addTab("成员", "member", TeamProjectMemberFragment.class, getBundle());
    }
    
    private Bundle getBundle() {
	Bundle bundle = new Bundle();
	bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, mTeam);
	bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT, mTeamProject);
	return bundle;
    }
}

