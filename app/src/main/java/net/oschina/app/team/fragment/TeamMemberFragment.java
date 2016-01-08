package net.oschina.app.team.fragment;

import java.util.List;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.cache.CacheManager;
import net.oschina.app.team.adapter.TeamMemberAdapter;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamMember;
import net.oschina.app.team.bean.TeamMemberList;
import net.oschina.app.team.ui.TeamMainActivity;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import org.kymjs.kjframe.http.KJAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 团队成员界面
 * 
 * @author kymjs (kymjs123@gmail.com)
 * 
 */
public class TeamMemberFragment extends BaseFragment {

    @InjectView(R.id.fragment_team_grid)
    GridView mGrid;
    @InjectView(R.id.fragment_team_empty)
    EmptyLayout mEmpty;
    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Activity aty;
    private Team team;
    private List<TeamMember> datas = null;
    private long preRefreshTime;
    private TeamMemberAdapter adapter;

    public static final String TEAM_MEMBER_FILE = "TeamMemberFragment_cache_file";
    public static String TEAM_MEMBER_KEY = "TeamMemberFragment_key";
    public static String TEAM_MEMBER_DATA = "TeamMemberFragment_key";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            team = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
        }

        TEAM_MEMBER_KEY += team.getId();
        TEAM_MEMBER_DATA += team.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_member,
                container, false);
        aty = getActivity();
        ButterKnife.inject(this, rootView);

        TeamMemberList list = (TeamMemberList) CacheManager.readObject(aty,
                TEAM_MEMBER_DATA);

        if (list == null) {
            initData();
        } else {
            datas = list.getList();
            adapter = new TeamMemberAdapter(aty, datas, team);
            mGrid.setAdapter(adapter);
        }

        initView(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {
        mEmpty.setErrorType(EmptyLayout.HIDE_LAYOUT);
        mEmpty.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refurbish(true);
            }
        });
        mGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                UIHelper.showTeamMemberInfo(aty, team.getId(),
                        datas.get(position));
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mState == STATE_REFRESH) {
                    return;
                }
                refurbish(false);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
    }

    @Override
    public void initData() {
        refurbish(true);
    }

    /**
     * 刷新列表数据
     */
    private void refurbish(final boolean isFirst) {
        final long currentTime = System.currentTimeMillis();
        if (currentTime - preRefreshTime < 100000) {
            setSwipeRefreshLoadedState();
            return;
        }
        OSChinaApi.getTeamMemberList(team.getId(),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        // setSwipeRefreshLoadingState();
                        if (isFirst) {
                            mEmpty.setErrorType(EmptyLayout.NETWORK_LOADING);
                        }
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1,
                            final byte[] arg2) {
                        KJAsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                TeamMemberList list = XmlUtils.toBean(
                                        TeamMemberList.class, arg2);
                                CacheManager.saveObject(aty, list,
                                        TEAM_MEMBER_DATA);
                                datas = list.getList();
                                aty.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (adapter == null) {
                                            adapter = new TeamMemberAdapter(
                                                    aty, datas, team);
                                            mGrid.setAdapter(adapter);
                                        } else {
                                            adapter.refresh(datas);
                                        }
                                        preRefreshTime = currentTime;
                                        if (isFirst) {
                                            mEmpty.setErrorType(EmptyLayout.HIDE_LAYOUT);
                                        }
                                        setSwipeRefreshLoadedState();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {
                        AppContext.showToast("成员信息获取失败");
                        if (isFirst) {
                            mEmpty.setErrorType(EmptyLayout.NETWORK_ERROR);
                        }
                        setSwipeRefreshLoadedState();
                    }
                });
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState() {
        mState = STATE_REFRESH;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setSwipeRefreshLoadedState() {
        mState = STATE_NOMORE;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

}
