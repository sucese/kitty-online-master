package net.oschina.app.team.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseActivity;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamList;
import net.oschina.app.team.viewpagefragment.TeamMainViewPagerFragment;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import org.kymjs.kjframe.utils.PreferenceHelper;
import org.kymjs.kjframe.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 团队主界面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月13日 下午3:36:56
 */

public class TeamMainActivity extends BaseActivity implements ActionBar.OnNavigationListener {

    private final String TEAM_LIST_FILE = "team_list_file";
    private final String TEAM_LIST_KEY = "team_list_key"
            + AppContext.getInstance().getLoginUid();

    public final static String BUNDLE_KEY_TEAM = "bundle_key_team";

    public final static String BUNDLE_KEY_PROJECT = "bundle_key_project";

    public final static String BUNDLE_KEY_ISSUE_CATALOG = "bundle_key_catalog_list";

    private final String tag = "team_view";

    private FragmentManager mFragmentManager;

    private int mCurrentContentIndex = -1;

    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;
    @InjectView(R.id.main_content)
    View container;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_team_main;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        // 隐藏actionbar的标题
        mActionBar.setDisplayShowTitleEnabled(false);
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        mErrorLayout.setErrorMessage("获取团队中...");
        mErrorLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestTeamList();
            }
        });

        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        adapter = new SpinnerAdapter(this, teamName);
        mActionBar.setListNavigationCallbacks(adapter, this);
        requestTeamList();

        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void initData() {
    }

    private SpinnerAdapter adapter;

    private final List<String> teamName = new ArrayList<String>();
    private List<Team> teamDatas = new ArrayList<Team>();

    /**
     * @param pos
     */
    private void switchTeam(int pos) {
        if (pos == mCurrentContentIndex)
            return;
        showWaitDialog("正在切换...");
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (tag != null) {
            Fragment fragment = mFragmentManager.findFragmentByTag(tag);
            if (fragment != null) {
                ft.remove(fragment);
            }
        }
        try {
            TeamMainViewPagerFragment fragment = TeamMainViewPagerFragment.class
                    .newInstance();
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_KEY_TEAM, teamDatas.get(pos));
            fragment.setArguments(bundle);
            ft.replace(R.id.main_content, fragment, tag);
            ft.commitAllowingStateLoss();
            mCurrentContentIndex = pos;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        hideWaitDialog();
    }

    private void requestTeamList() {
        // 初始化团队列表数据
        String cache = PreferenceHelper.readString(this, TEAM_LIST_FILE,
                TEAM_LIST_KEY, "");
        if (!StringUtils.isEmpty(cache)) {
            // mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            teamDatas = TeamList.toTeamList(cache);
            setTeamDataState();
        } else {
            //mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }

        OSChinaApi.teamList(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                TeamList datas = XmlUtils.toBean(TeamList.class, arg2);
                if (teamDatas.isEmpty() && datas != null) {
                    teamDatas.addAll(datas.getList());
                    setTeamDataState();
                } else {
                    if (teamDatas == null && datas == null) {
                        AppContext.showToast(new String(arg2));
                        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                        mErrorLayout.setErrorMessage("获取团队失败");
                    }
                }

                if (datas != null) {
                    // 保存新的团队列表
                    PreferenceHelper.write(TeamMainActivity.this, TEAM_LIST_FILE,
                            TEAM_LIST_KEY, datas.toCacheData());
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                //AppContext.showToast("网络不好，请稍后重试");
            }
        });
    }

    private void setTeamDataState() {
        if (teamDatas == null) {
            teamDatas = new ArrayList<Team>();
        }
        if (teamDatas.isEmpty()) {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
            String msg = getResources().getString(R.string.team_empty);
            mErrorLayout.setErrorMessage(msg);
            mErrorLayout.setErrorImag(R.drawable.page_icon_empty);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            container.setVisibility(View.VISIBLE);
        }
        for (Team team : this.teamDatas) {
            teamName.add(team.getName());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        Team team = teamDatas.get(itemPosition);
        if (team != null) {
            switchTeam(itemPosition);
            adapter.setSelectIndex(itemPosition);
        }

        return false;
    }

    public class SpinnerAdapter extends BaseAdapter {

        private final List<String> teams;

        private final Context context;

        private int selectIndex = 0;

        public void setSelectIndex(int index) {
            this.selectIndex = index;
        }

        public SpinnerAdapter(Context context, List<String> teams) {
            this.teams = teams;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.spinner_layout_head, null);
            }
            ((TextView) convertView).setText(getItem(position));

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_cell_team, null, false);
            }
            String team = getItem(position);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
            if (team != null) {
                tv.setText(team);
            }
            if (selectIndex != position) {
                tv.setTextColor(Color.parseColor("#acd4b3"));
            } else {
                tv.setTextColor(Color.parseColor("#6baf77"));
            }
            return convertView;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return teams.size();
        }

        @Override
        public void notifyDataSetChanged() {
            // TODO Auto-generated method stub
            super.notifyDataSetChanged();
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return teams.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

    }
}
