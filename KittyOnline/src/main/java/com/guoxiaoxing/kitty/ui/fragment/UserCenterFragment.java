package com.guoxiaoxing.kitty.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.ActiveAdapter;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.base.BaseFragment;
import com.guoxiaoxing.kitty.base.ListBaseAdapter;
import com.guoxiaoxing.kitty.bean.Active;
import com.guoxiaoxing.kitty.bean.Result;
import com.guoxiaoxing.kitty.bean.ResultBean;
import com.guoxiaoxing.kitty.bean.User;
import com.guoxiaoxing.kitty.bean.UserInformation;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.DialogHelp;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TDevice;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.guoxiaoxing.kitty.widget.AvatarView;


import java.io.ByteArrayInputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月29日 下午2:33:18
 * 
 */

public class UserCenterFragment extends BaseFragment implements
        OnItemClickListener, OnScrollListener {

    private static final Object FEMALE = "女";

    @InjectView(R.id.error_layout)
    EmptyLayout mEmptyView;

    @InjectView(R.id.lv_user_active)
    ListView mListView;
    private ImageView mIvAvatar, mIvGender;
    private TextView mTvName, mTvFollowing, mTvFollower, mTvSore,
            mBtnPrivateMsg, mBtnFollowUser, mTvLastestLoginTime;

    private ActiveAdapter mAdapter;
    private int mHisUid;
    private String mHisName;
    private int mUid;
    private User mUser;

    private int mActivePage = 0;

    private final AsyncHttpResponseHandler mActiveHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            try {
                UserInformation information = XmlUtils.toBean(
                        UserInformation.class, new ByteArrayInputStream(arg2));
                mUser = information.getUser();
                fillUI();
                List<Active> data = information.getActiveList();
                if (mState == STATE_REFRESH)
                    mAdapter.clear();
                mAdapter.addData(data);
                mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
                if (data.size() == 0 && mState == STATE_REFRESH) {
                    mEmptyView.setErrorType(EmptyLayout.NODATA);
                    mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
                } else if (data.size() == 0) {
                    if (mState == STATE_REFRESH)
                        mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
                    else
                        mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
                } else {
                    mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
                }
            } catch (Exception e) {
                onFailure(arg0, arg1, arg2, e);
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                Throwable arg3) {
            mEmptyView.setErrorType(EmptyLayout.NETWORK_ERROR);
        }

        @Override
        public void onFinish() {
            mState = STATE_NONE;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_user_center, container,
                false);

        Bundle args = getArguments();

        mHisUid = args.getInt("his_id", 0);
        mHisName = args.getString("his_name");
        mUid = AppContext.getInstance().getLoginUid();
        ButterKnife.inject(this, view);
        initView(view);

        return view;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
        case R.id.iv_avatar:
            UIHelper.showUserAvatar(getActivity(), mUser.getPortrait());
            break;
        case R.id.ly_following:
            UIHelper.showFriends(getActivity(), mUser.getId(), 0);
            break;
        case R.id.ly_follower:
            UIHelper.showFriends(getActivity(), mUser.getId(), 1);
            break;
        case R.id.tv_follow_user:
            handleUserRelation();
            break;
        case R.id.tv_private_message:
            if (mHisUid == AppContext.getInstance().getLoginUid()) {
                AppContext.showToast("不能给自己发送留言:)");
                return;
            }
            if (!AppContext.getInstance().isLogin()) {
                UIHelper.showLoginActivity(getActivity());
                return;
            }
            UIHelper.showMessageDetail(getActivity(), mHisUid, mHisName);
            break;
        case R.id.tv_blog:
            UIHelper.showUserBlog(getActivity(), mHisUid);
            break;
        case R.id.tv_information:
            showInformationDialog();
            break;
        default:
            break;
        }
    }

    @Override
    public void initView(View view) {
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);

        View header = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_user_center_header, null, false);

        mIvAvatar = (ImageView) header.findViewById(R.id.iv_avatar);
        mIvAvatar.setOnClickListener(this);
        mIvGender = (ImageView) header.findViewById(R.id.iv_gender);
        mTvName = (TextView) header.findViewById(R.id.tv_name);
        mTvFollowing = (TextView) header.findViewById(R.id.tv_following_count);
        header.findViewById(R.id.ly_following).setOnClickListener(this);
        mTvFollower = (TextView) header.findViewById(R.id.tv_follower_count);
        header.findViewById(R.id.ly_follower).setOnClickListener(this);
        mTvSore = (TextView) header.findViewById(R.id.tv_score);
        mTvLastestLoginTime = (TextView) header
                .findViewById(R.id.tv_latest_login_time);

        mBtnPrivateMsg = (TextView) header
                .findViewById(R.id.tv_private_message);
        mBtnPrivateMsg.setOnClickListener(this);
        mBtnFollowUser = (TextView) header.findViewById(R.id.tv_follow_user);
        mBtnFollowUser.setOnClickListener(this);

        header.findViewById(R.id.tv_blog).setOnClickListener(this);
        header.findViewById(R.id.tv_information).setOnClickListener(this);

        mListView.addHeaderView(header);

        mBtnPrivateMsg.setOnClickListener(this);
        mBtnFollowUser.setOnClickListener(this);

        if (mAdapter == null) {
            mAdapter = new ActiveAdapter();

            fristSendGetUserInfomation();
        }
        mListView.setAdapter(mAdapter);

        mEmptyView.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fristSendGetUserInfomation();
            }
        });
    }

    private void fristSendGetUserInfomation() {
        mState = STATE_REFRESH;
        mListView.setVisibility(View.GONE);
        mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
        sendGetUserInfomation();
    }

    private void sendGetUserInfomation() {
        OSChinaApi.getUserInformation(mUid, mHisUid, mHisName, mActivePage,
                mActiveHandler);
    }

    private void fillUI() {
        mListView.setVisibility(View.VISIBLE);
        ((AvatarView) mIvAvatar).setAvatarUrl(mUser.getPortrait());
        mHisUid = mUser.getId();
        mHisName = mUser.getName();
        mTvName.setText(mHisName);

        int genderIcon = R.drawable.userinfo_icon_male;
        if (FEMALE.equals(mUser.getGender())) {
            genderIcon = R.drawable.userinfo_icon_female;
        }
        mIvGender.setBackgroundResource(genderIcon);

        mTvFollowing.setText(mUser.getFollowers() + "");
        mTvFollower.setText(mUser.getFans() + "");
        mTvSore.setText(mUser.getScore() + "");
        mTvLastestLoginTime.setText(getString(R.string.latest_login_time,
                StringUtils.friendly_time(mUser.getLatestonline())));
        updateUserRelation();
    }

    private void updateUserRelation() {
        switch (mUser.getRelation()) {
        case User.RELATION_TYPE_BOTH:
            mBtnFollowUser.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_follow_each_other, 0, 0, 0);
            mBtnFollowUser.setText(R.string.follow_each_other);
            mBtnFollowUser.setTextColor(getResources().getColor(R.color.black));
            mBtnFollowUser
                    .setBackgroundResource(R.drawable.btn_small_white_selector);
            break;
        case User.RELATION_TYPE_FANS_HIM:
            mBtnFollowUser.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_followed, 0, 0, 0);
            mBtnFollowUser.setText(R.string.unfollow_user);
            mBtnFollowUser.setTextColor(getResources().getColor(R.color.black));
            mBtnFollowUser
                    .setBackgroundResource(R.drawable.btn_small_white_selector);
            break;
        case User.RELATION_TYPE_FANS_ME:
            mBtnFollowUser.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_add_follow, 0, 0, 0);
            mBtnFollowUser.setText(R.string.follow_user);
            mBtnFollowUser.setTextColor(getResources().getColor(R.color.white));
            mBtnFollowUser
                    .setBackgroundResource(R.drawable.btn_small_green_selector);
            break;
        case User.RELATION_TYPE_NULL:
            mBtnFollowUser.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_add_follow, 0, 0, 0);
            mBtnFollowUser.setText(R.string.follow_user);
            mBtnFollowUser.setTextColor(getResources().getColor(R.color.white));
            mBtnFollowUser
                    .setBackgroundResource(R.drawable.btn_small_green_selector);
            break;
        }
        int padding = (int) TDevice.dpToPixel(20);
        mBtnFollowUser.setPadding(padding, 0, padding, 0);
    }

    private AlertDialog mInformationDialog;

    private void showInformationDialog() {
        if (mInformationDialog == null) {
            mInformationDialog = DialogHelp.getDialog(getActivity()).show();
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.fragment_user_center_information, null);
            ((TextView) view.findViewById(R.id.tv_join_time))
                    .setText(StringUtils.friendly_time(mUser.getJointime()));
            ((TextView) view.findViewById(R.id.tv_location))
                    .setText(StringUtils.getString(mUser.getFrom()));
            ((TextView) view.findViewById(R.id.tv_development_platform))
                    .setText(StringUtils.getString(mUser.getDevplatform()));
            ((TextView) view.findViewById(R.id.tv_academic_focus))
                    .setText(StringUtils.getString(mUser.getExpertise()));
            mInformationDialog.setContentView(view);
        }

        mInformationDialog.show();
    }

    private void handleUserRelation() {
        if (mUser == null)
            return;
        // 判断登录
        final AppContext ac = AppContext.getInstance();
        if (!ac.isLogin()) {
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        String dialogTitle = "";
        int relationAction = 0;
        switch (mUser.getRelation()) {
        case User.RELATION_TYPE_BOTH:
            dialogTitle = "确定取消互粉吗？";
            relationAction = User.RELATION_ACTION_DELETE;
            break;
        case User.RELATION_TYPE_FANS_HIM:
            dialogTitle = "确定取消关注吗？";
            relationAction = User.RELATION_ACTION_DELETE;
            break;
        case User.RELATION_TYPE_FANS_ME:
            dialogTitle = "确定关注Ta吗？";
            relationAction = User.RELATION_ACTION_ADD;
            break;
        case User.RELATION_TYPE_NULL:
            dialogTitle = "确定关注Ta吗？";
            relationAction = User.RELATION_ACTION_ADD;
            break;
        }
        final int ra = relationAction;

        DialogHelp.getConfirmDialog(getActivity(), dialogTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendUpdateRelcationRequest(ra);
            }
        }).show();
    }

    private void sendUpdateRelcationRequest(int ra) {
        OSChinaApi.updateRelation(mUid, mHisUid, ra,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        try {
                            Result result = XmlUtils.toBean(ResultBean.class,
                                    new ByteArrayInputStream(arg2)).getResult();
                            if (result.OK()) {
                                switch (mUser.getRelation()) {
                                case User.RELATION_TYPE_BOTH:
                                    mBtnFollowUser
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    R.drawable.ic_add_follow,
                                                    0, 0, 0);
                                    mBtnFollowUser
                                            .setText(R.string.follow_user);
                                    mBtnFollowUser.setTextColor(getResources()
                                            .getColor(R.color.white));
                                    mBtnFollowUser
                                            .setBackgroundResource(R.drawable.btn_small_green_selector);
                                    mUser.setRelation(User.RELATION_TYPE_FANS_ME);
                                    break;
                                case User.RELATION_TYPE_FANS_HIM:
                                    mBtnFollowUser
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    R.drawable.ic_add_follow,
                                                    0, 0, 0);
                                    mBtnFollowUser
                                            .setText(R.string.follow_user);
                                    mBtnFollowUser.setTextColor(getResources()
                                            .getColor(R.color.white));
                                    mBtnFollowUser
                                            .setBackgroundResource(R.drawable.btn_small_green_selector);
                                    mUser.setRelation(User.RELATION_TYPE_NULL);
                                    break;
                                case User.RELATION_TYPE_FANS_ME:
                                    mBtnFollowUser
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    R.drawable.ic_followed, 0,
                                                    0, 0);
                                    mBtnFollowUser
                                            .setText(R.string.follow_each_other);
                                    mBtnFollowUser.setTextColor(getResources()
                                            .getColor(R.color.black));
                                    mBtnFollowUser
                                            .setBackgroundResource(R.drawable.btn_small_white_selector);
                                    mUser.setRelation(User.RELATION_TYPE_BOTH);
                                    break;
                                case User.RELATION_TYPE_NULL:
                                    mBtnFollowUser
                                            .setCompoundDrawablesWithIntrinsicBounds(
                                                    R.drawable.ic_followed, 0,
                                                    0, 0);
                                    mBtnFollowUser
                                            .setText(R.string.unfollow_user);
                                    mBtnFollowUser.setTextColor(getResources()
                                            .getColor(R.color.black));
                                    mBtnFollowUser
                                            .setBackgroundResource(R.drawable.btn_small_white_selector);
                                    mUser.setRelation(User.RELATION_TYPE_FANS_HIM);
                                    break;
                                }
                                int padding = (int) TDevice.dpToPixel(20);
                                mBtnFollowUser.setPadding(padding, 0, padding,
                                        0);
                            }
                            AppContext.showToastShort(result.getErrorMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            onFailure(arg0, arg1, arg2, e);
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {}
                });
    }

    @Override
    public void initData() {}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        if (position - 1 < 0) {
            return;
        }
        Active active = (Active) mAdapter.getItem(position - 1);
        if (active != null)
            UIHelper.showActiveRedirect(view.getContext(), active);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // 数据已经全部加载，或数据为空时，或正在加载，不处理滚动事件
        if (mState == STATE_NOMORE || mState == STATE_LOADMORE
                || mState == STATE_REFRESH) {
            return;
        }
        if (mAdapter != null
                && mAdapter.getDataSize() > 0
                && mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
            if (mState == STATE_NONE
                    && mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
                mState = STATE_LOADMORE;
                mActivePage++;
                sendGetUserInfomation();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}
}
