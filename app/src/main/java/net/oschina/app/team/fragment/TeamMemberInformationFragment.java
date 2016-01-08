package net.oschina.app.team.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseListFragment;
import net.oschina.app.team.adapter.TeamActiveAdapter;
import net.oschina.app.team.adapter.TeamMemberAdapter;
import net.oschina.app.team.bean.TeamActive;
import net.oschina.app.team.bean.TeamActives;
import net.oschina.app.team.bean.TeamMember;
import net.oschina.app.ui.DetailActivity;
import net.oschina.app.ui.SimpleBackActivity;
import net.oschina.app.util.TLog;
import net.oschina.app.util.XmlUtils;
import net.oschina.app.widget.AvatarView;

import org.kymjs.kjframe.utils.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户个人信息界面
 * 
 * @author kymjs (https://github.com/kymjs)
 * 
 */
public class TeamMemberInformationFragment extends BaseListFragment<TeamActive> {

    private TextView mTvName;
    private TextView mTvUserName;
    private TextView mTvEmail;
    private TextView mTvTel;
    private ImageView mImgTel;
    private TextView mTvAddress;
    private AvatarView mImgHead;
    // private TextView mTvToTel;

    private Activity aty;
    private TeamMember teamMember;
    private int teamId;

    protected static final String TAG = TeamMemberInformationFragment.class
            .getSimpleName();
    private static final String CACHE_KEY_PREFIX = "DynamicFragment_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getBundleExtra(
                SimpleBackActivity.BUNDLE_KEY_ARGS);
        if (bundle != null) {
            teamMember = (TeamMember) bundle
                    .getSerializable(TeamMemberAdapter.TEAM_MEMBER_KEY);
            teamId = bundle.getInt(TeamMemberAdapter.TEAM_ID_KEY);
        } else {
            teamMember = new TeamMember();
            TLog.log(TAG, "数据初始化异常");
        }
        aty = getActivity();
    }

    @Override
    public void initView(View view) {
        View headview = View.inflate(aty, R.layout.fragment_team_userinfo_head,
                null);
        mImgTel = (ImageView) headview.findViewById(R.id.fragment_team_tv_tel);
        // mTvToTel = (TextView)
        // headview.findViewById(R.id.fragment_team_tv_tel);
        // TypefaceUtils.setTypeface(mTvToTel);
        // mTvToTel.setOnClickListener(new OnClickListener() {
        mImgTel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + teamMember.getTeamTelephone()));

                startActivity(intent);
            }
        });
        mImgHead = (AvatarView) headview.findViewById(R.id.fragment_team_head);
        mTvName = (TextView) headview.findViewById(R.id.fragment_team_name);
        mTvUserName = (TextView) headview
                .findViewById(R.id.fragment_team_username);
        mTvEmail = (TextView) headview.findViewById(R.id.fragment_team_email);
        mTvTel = (TextView) headview.findViewById(R.id.fragment_team_tel);
        mTvAddress = (TextView) headview
                .findViewById(R.id.fragment_team_address);
        mListView.addHeaderView(headview);

        mTvName.setText(teamMember.getName());
        mTvUserName.setText(teamMember.getOscName());

        String email = teamMember.getTeamEmail();
        if (StringUtils.isEmpty(email)) {
            email = "未填写邮箱";
        }
        mTvEmail.setText(email);
        String tel = teamMember.getTeamTelephone();
        if (StringUtils.isEmpty(tel)) {
            tel = "未填写手机号";
            mImgTel.setVisibility(View.GONE);
            // mTvToTel.setVisibility(View.GONE);
        }
        mTvTel.setText(tel);
        mTvAddress.setText(teamMember.getLocation());
        mImgHead.setAvatarUrl(teamMember.getPortrait());
        super.initView(view);
        mListView.setSelector(new ColorDrawable(android.R.color.transparent));
        mListView.setDivider(new ColorDrawable(android.R.color.transparent));

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                if (position == 0) {// 第一项是头部
                } else {
                    Adapter adapter = parent.getAdapter();
                    if (adapter != null) {
                        Object obj = adapter.getItem(position);
                        TeamActive data = null;
                        if (obj instanceof TeamActive) {
                            data = (TeamActive) obj;
                        }
                        Intent intent = new Intent(aty, DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(
                                TeamActiveFragment.DYNAMIC_FRAGMENT_KEY, data);
                        bundle.putInt(
                                TeamActiveFragment.DYNAMIC_FRAGMENT_TEAM_KEY,
                                teamId);
                        bundle.putInt(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE,
                                DetailActivity.DISPLAY_TEAM_TWEET_DETAIL);
                        intent.putExtras(bundle);
                        aty.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected TeamActiveAdapter getListAdapter() {
        return new TeamActiveAdapter(aty);
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + "_" + teamMember.getId() + mCurrentPage;
    }

    @Override
    protected TeamActives parseList(InputStream is) throws Exception {
        TeamActives list = XmlUtils.toBean(TeamActives.class, is);
        return list;
    }

    @Override
    protected TeamActives readList(Serializable seri) {
        return (TeamActives) seri;
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getUserDynamic(teamId, teamMember.getId() + "",
                mCurrentPage, mHandler);
    }
}
