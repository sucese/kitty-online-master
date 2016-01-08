package net.oschina.app.widget;

import java.util.List;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.cache.CacheManager;
import net.oschina.app.team.adapter.TeamDiaryListAdapter;
import net.oschina.app.team.bean.TeamDiary;
import net.oschina.app.team.bean.TeamDiaryList;
import net.oschina.app.team.viewpagefragment.TeamDiaryFragment;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import org.kymjs.kjframe.http.KJAsyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 周报每个Page,单独拿出来写
 * 
 * @author kymjs (http://www.kymjs.com)
 */
public class DiaryPageContentView {

    private final RelativeLayout rootView;
    private final ListView listview;
    private final SwipeRefreshLayout pullHeadView;
    private final EmptyLayout errorLayout;
    private final Activity cxt;

    private final int teamId;
    private final int year;
    private final int week;

    private TeamDiaryList datas;
    private final TeamDiaryListAdapter adapter;

    /** 只允许new View的形式创建 */
    public DiaryPageContentView(Context context, int teamId, int year, int week) {
        this.teamId = teamId;
        this.year = year;
        this.week = week;
        this.cxt = (Activity) context;

        rootView = (RelativeLayout) View.inflate(context,
                R.layout.pager_item_diary, null);
        listview = (ListView) rootView.findViewById(R.id.diary_listview);
        pullHeadView = (SwipeRefreshLayout) rootView
                .findViewById(R.id.swiperefreshlayout);
        errorLayout = (EmptyLayout) rootView.findViewById(R.id.error_layout);
        adapter = new TeamDiaryListAdapter(cxt, null);
        initView();
        requestData(true);
    }

    private void initView() {
        errorLayout.setOnLayoutClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(true);
            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Bundle args = new Bundle();
                args.putInt(TeamDiaryFragment.TEAMID_KEY, teamId);
                args.putSerializable(TeamDiaryFragment.DIARYDETAIL_KEY, datas
                        .getList().get(position));
                UIHelper.showDiaryDetail(cxt, args);
            }
        });

        pullHeadView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (BaseFragment.mState == BaseFragment.STATE_REFRESH) {
                    return;
                } else {
                    errorLayout.setErrorMessage("本周无人提交周报");
                    // // 设置顶部正在刷新
                    // setSwipeRefreshLoadingState(pullHeadView);
                    requestData(false);
                }
            }
        });
        pullHeadView.setColorSchemeResources(R.color.swiperefresh_color1,
                R.color.swiperefresh_color2, R.color.swiperefresh_color3,
                R.color.swiperefresh_color4);
    }

    private void requestData(final boolean isFirst) {
        OSChinaApi.getDiaryFromWhichWeek(teamId, year, week,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        setSwipeRefreshLoadingState(pullHeadView);
                        if (isFirst) {
                            errorLayout
                                    .setErrorType(EmptyLayout.NETWORK_LOADING);
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {
                        /* 网络异常 */
                        if (errorLayout != null) {
                            errorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                            errorLayout.setVisibility(View.VISIBLE);
                        }
                        setSwipeRefreshLoadedState(pullHeadView);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1,
                            final byte[] arg2) {
                        KJAsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                datas = XmlUtils.toBean(TeamDiaryList.class,
                                        arg2);
                                CacheManager.saveObject(cxt, datas,
                                        "TeamDiaryPagerFragment" + week);

                                cxt.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final List<TeamDiary> tempData = datas
                                                .getList();
                                        if (tempData == null
                                                || tempData.isEmpty()) {
                                            errorLayout
                                                    .setNoDataContent("本周无人提交周报");
                                            errorLayout
                                                    .setErrorType(EmptyLayout.NODATA);
                                            errorLayout
                                                    .setVisibility(View.VISIBLE);
                                        } else {
                                            errorLayout
                                                    .setVisibility(View.GONE);
                                            adapter.refresh(tempData);
                                            listview.setAdapter(adapter);
                                        }
                                        setSwipeRefreshLoadedState(pullHeadView);
                                    }
                                });
                            }
                        });
                    }
                });
    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState(
            SwipeRefreshLayout mSwipeRefreshLayout) {
        BaseFragment.mState = BaseFragment.STATE_REFRESH;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            // 防止多次重复刷新
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    public RelativeLayout getView() {
        return rootView;
    }

    /**
     * 设置顶部加载完毕的状态
     */
    private void setSwipeRefreshLoadedState(
            SwipeRefreshLayout mSwipeRefreshLayout) {
        BaseFragment.mState = BaseFragment.STATE_NOMORE;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }
}
