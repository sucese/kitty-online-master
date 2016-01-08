package net.oschina.app.team.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.team.bean.MyIssueState;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.ui.TeamMainActivity;
import net.oschina.app.util.TypefaceUtils;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;
import net.oschina.app.widget.AvatarView;


import org.kymjs.kjframe.utils.SystemTool;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Team面板界面
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class TeamBoardFragment extends BaseFragment {

    @InjectView(R.id.team_myissue_waitdo)
    View mRlWaitDo;
    @InjectView(R.id.team_myissue_outdate)
    View mRlWill;
    @InjectView(R.id.team_myissue_ing)
    View mRlIng;
    @InjectView(R.id.team_myissue_all)
    View mRlAll;

    @InjectView(R.id.team_myissue_wait_num)
    TextView mTvWaitDo;
    @InjectView(R.id.team_myissue_outdate_num)
    TextView mTvOutdate;
    @InjectView(R.id.team_myissue_ing_num)
    TextView mTvIng;
    @InjectView(R.id.team_myissue_all_num)
    TextView mTvAll;

    @InjectView(R.id.iv_avatar)
    AvatarView mIvAvatarView;
    @InjectView(R.id.team_myissue_name)
    TextView mTvName;
    @InjectView(R.id.team_myissue_date)
    TextView mTvDate;

    private Team team;

    public static final String WHICH_PAGER_KEY = "MyIssueFragment_wihch_pager";

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            team = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_team_board,
                container, false);
        ButterKnife.inject(this, rootView);
        initData();
        initView(rootView);
        return rootView;
    }

    @Override
    public void initView(View view) {
        mRlWaitDo.setOnClickListener(this);
        mRlWill.setOnClickListener(this);
        mRlIng.setOnClickListener(this);
        mRlAll.setOnClickListener(this);

        mTvName.setText(AppContext.getInstance().getLoginUser().getName() + "，"
                + getGreetings());
        mIvAvatarView.setAvatarUrl(AppContext.getInstance().getLoginUser()
                .getPortrait());
        mTvDate.setText("今天是 " + getWeekDay() + "，"
                + SystemTool.getDataTime("yyyy年MM月dd日"));

        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_team_active));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_team_project));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_team_issue));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_team_discuss));
        TypefaceUtils.setTypeface((TextView) view
                .findViewById(R.id.tv_team_diary));
    }

    private String getGreetings() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 6) {
            return "凌晨好!";
        } else if (hour < 9) {
            return "早上好!";
        } else if (hour < 12) {
            return "上午好!";
        } else if (hour < 14) {
            return "中午好!";
        } else if (hour < 17) {
            return "下午好!";
        } else if (hour < 19) {
            return "傍晚好!";
        } else if (hour < 22) {
            return "晚上好!";
        } else {
            return "夜里好!";
        }
    }

    private String getWeekDay() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String weekStr = "";
        switch (dayOfWeek) {
        case 1:
            weekStr = "星期日";
            break;
        case 2:
            weekStr = "星期一";
            break;
        case 3:
            weekStr = "星期二";
            break;
        case 4:
            weekStr = "星期三";
            break;
        case 5:
            weekStr = "星期四";
            break;
        case 6:
            weekStr = "星期五";
            break;
        case 7:
            weekStr = "星期六";
            break;
        }

        return weekStr;
    }

    @Override
    @OnClick({ R.id.ll_team_active, R.id.ll_team_project, R.id.ll_team_issue,
            R.id.ll_team_discuss, R.id.ll_team_diary })
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.team_myissue_waitdo:
            UIHelper.showSimpleBack(getActivity(),
                    SimpleBackPage.MY_ISSUE_PAGER, getMyIssueStateBundle(0));
            break;
        case R.id.team_myissue_ing:
            UIHelper.showSimpleBack(getActivity(),
                    SimpleBackPage.MY_ISSUE_PAGER, getMyIssueStateBundle(1));
            break;
        case R.id.team_myissue_outdate:
            UIHelper.showSimpleBack(getActivity(),
                    SimpleBackPage.MY_ISSUE_PAGER, getMyIssueStateBundle(0));
            break;
        case R.id.team_myissue_all:
            UIHelper.showSimpleBack(getActivity(),
                    SimpleBackPage.MY_ISSUE_PAGER, getMyIssueStateBundle(2));
            break;
        case R.id.ll_team_active:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_ACTIVE,
                    getArguments());
            break;
        case R.id.ll_team_project:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_PROJECT,
                    getArguments());
            break;
        case R.id.ll_team_issue:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_ISSUE,
                    getBundle());
            break;
        case R.id.ll_team_discuss:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_DISCUSS,
                    getBundle());
            break;
        case R.id.ll_team_diary:
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TEAM_DIRAY,
                    getBundle());
            break;
        default:
            break;
        }
    }

    private Bundle getMyIssueStateBundle(int index) {
        Bundle bundle = getBundle();
        bundle.putInt(WHICH_PAGER_KEY, index);
        return bundle;
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, team);
        return bundle;
    }

    @Override
    public void initData() {
        requestData();
    }

    private void requestData() {
        OSChinaApi.getMyIssueState(team.getId() + "", AppContext.getInstance()
                .getLoginUid() + "", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                MyIssueState data = XmlUtils.toBean(MyIssueState.class,
                        new ByteArrayInputStream(arg2));
                if (data != null) {
                    fillUI(data);
                } else {
                    // 用户未登陆的情况
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                    Throwable arg3) {}

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }
        });
    }

    public void refresh() {
        requestData();
    }

    private void fillUI(MyIssueState data) {
        try {
            mTvName.setText(data.getUser().getName() + "，" + getGreetings());
        } catch (NullPointerException e) {
            mTvName.setText("哈喽，" + getGreetings());
        }
        mTvWaitDo.setText(data.getOpened());
        mTvOutdate.setText(data.getOutdate());
        mTvIng.setText(data.getUnderway());
        mTvAll.setText(data.getFinished());
    }
}
