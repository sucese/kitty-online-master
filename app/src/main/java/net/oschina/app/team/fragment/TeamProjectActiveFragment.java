package net.oschina.app.team.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaTeamApi;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.base.ListBaseAdapter;
import net.oschina.app.team.adapter.TeamActiveAdapter;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamActive;
import net.oschina.app.team.bean.TeamActives;
import net.oschina.app.team.bean.TeamProject;
import net.oschina.app.team.ui.TeamMainActivity;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 团队动态列表 TeamProjectFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 * 
 * @data 2015-2-28 下午4:08:58
 */
public class TeamProjectActiveFragment extends BaseListFragment<TeamActive> {

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

            mTeamProject = (TeamProject) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT);

            mTeamId = mTeam.getId();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setSelector(new ColorDrawable(android.R.color.transparent));
        mListView.setDivider(new ColorDrawable(android.R.color.transparent));
    }

    @Override
    protected TeamActiveAdapter getListAdapter() {
        // TODO Auto-generated method stub
        return new TeamActiveAdapter(getActivity());
    }

    @Override
    protected String getCacheKeyPrefix() {
        return "team_project_active_list_" + mTeamId + "_"
                + mTeamProject.getGit().getId();
    }

    @Override
    protected TeamActives parseList(InputStream is) throws Exception {
        TeamActives list = XmlUtils.toBean(TeamActives.class, is);
        return list;
    }

    @Override
    protected TeamActives readList(Serializable seri) {
        return ((TeamActives) seri);
    }

    @Override
    protected void sendRequestData() {
        // TODO Auto-generated method stub
        OSChinaTeamApi.getTeamProjectActiveList(mTeamId, mTeamProject, "all",
                mCurrentPage, mHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(List<TeamActive> data) {
        // TODO Auto-generated method stub
        super.executeOnLoadDataSuccess(data);
        if (mAdapter.getData().isEmpty()) {
            setNoProjectActive();
        }
        mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
    }

    private void setNoProjectActive() {
        mErrorLayout.setErrorType(EmptyLayout.NODATA);
        mErrorLayout.setErrorImag(R.drawable.page_icon_empty);
        String str = getResources().getString(
                R.string.team_empty_project_active);
        mErrorLayout.setErrorMessage(str);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        // TODO Auto-generated method stub
        TeamActive active = mAdapter.getItem(position);
        if (active != null) {
            UIHelper.showTeamActiveDetail(getActivity(), mTeam.getId(), active);
        }
    }
}
