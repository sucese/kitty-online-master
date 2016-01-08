package net.oschina.app.team.fragment;

import java.io.ByteArrayInputStream;
import java.util.List;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaTeamApi;
import net.oschina.app.team.adapter.TeamProjectListAdapter;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamGit;
import net.oschina.app.team.bean.TeamProject;
import net.oschina.app.team.bean.TeamProjectList;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 任务列表项目选择
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月19日 下午4:31:59
 * 
 */
public class TeamProjectSelectPopupWindow extends PopupWindow implements
        OnItemClickListener {
	

    public interface TeamProjectPopupWindowCallBack {
        public void callBack(TeamProject teamProject);
    }

    private final Team mTeam;

    private LayoutInflater inflater;

    private ListView mList;

    private EmptyLayout mErrorLayout;

    private TeamProjectListAdapter mAdapter;

    private final TeamProjectPopupWindowCallBack mCallBack;

    private TeamProject mCurrentProject;

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            TeamProjectList teamProjectList = XmlUtils.toBean(
                    TeamProjectList.class, new ByteArrayInputStream(arg2));
            loadSuccess(teamProjectList.getList());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mErrorLayout.setErrorMessage("" + teamProjectList.getList().size());
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                Throwable arg3) {}

        @Override
        public void onFinish() {
            super.onFinish();
        }

        @Override
        public void onStart() {
            super.onStart();
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }
    };

    public TeamProjectSelectPopupWindow(final Activity context, Team team,
            TeamProjectPopupWindowCallBack callBack) {
        super(context);
        this.mTeam = team;
        this.mCallBack = callBack;
        initView(context);
        initData();
    }

    private void initView(Context context) {
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(
                R.layout.popup_window_team_projects_select, null);
        mList = (ListView) view.findViewById(R.id.lv_list);
        mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 所有的的DropDownMenu的根的ID都需要时set_up
                int height = view.findViewById(R.id.set_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        mList.setOnItemClickListener(this);
    }

    private void initData() {
        mAdapter = new TeamProjectListAdapter();
        mList.setAdapter(mAdapter);
        getProjectsList();

    }

    private void loadSuccess(List<TeamProject> list) {
        addAllIssueOption(list);
        mAdapter.add(list);
    }

    private void getProjectsList() {
        OSChinaTeamApi.getTeamProjectList(mTeam.getId(), mHandler);
    }
    
    private void addAllIssueOption(List<TeamProject> list) {

        TeamProject unProjectIssue = new TeamProject();
        TeamGit unGit = new TeamGit();
        unProjectIssue.setSource("");
        unGit.setId(0);// 0表示非项目任务
        unGit.setName("非项目任务");
        unProjectIssue.setGit(unGit);

        list.add(0, unProjectIssue);

        TeamProject allIssue = new TeamProject();
        TeamGit allGit = new TeamGit();
        allIssue.setSource("");
        allGit.setId(-1);// -1表示
        allGit.setName("所有任务");
        allIssue.setGit(allGit);

        list.add(0, allIssue);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        TeamProject project = mAdapter.getItem(position);
        if (mCallBack != null && project != null) {
            mCallBack.callBack(project);
        }
        this.dismiss();
    }
}
