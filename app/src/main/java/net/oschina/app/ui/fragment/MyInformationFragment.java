package net.oschina.app.ui.fragment;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.Constants;
import net.oschina.app.bean.MyInformation;
import net.oschina.app.bean.Notice;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.bean.User;
import net.oschina.app.cache.CacheManager;
import net.oschina.app.ui.MainActivity;
import net.oschina.app.ui.MyQrodeDialog;
import net.oschina.app.ui.empty.EmptyLayout;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.XmlUtils;
import net.oschina.app.widget.AvatarView;
import net.oschina.app.widget.BadgeView;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 登录用户中心页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author kymjs (http://my.oschina.net/kymjs)
 * @version 创建时间：2014年10月30日 下午4:05:47
 */
public class MyInformationFragment extends BaseFragment {

    public static final int sChildView = 9; // 在没有加入TeamList控件时rootview有多少子布局

    @InjectView(R.id.iv_avatar)
    AvatarView mIvAvatar;
    @InjectView(R.id.iv_gender)
    ImageView mIvGender;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    @InjectView(R.id.tv_score)
    TextView mTvScore;
    @InjectView(R.id.tv_favorite)
    TextView mTvFavorite;
    @InjectView(R.id.tv_following)
    TextView mTvFollowing;
    @InjectView(R.id.tv_follower)
    TextView mTvFans;
    @InjectView(R.id.tv_mes)
    View mMesView;
    @InjectView(R.id.error_layout)
    EmptyLayout mErrorLayout;
    @InjectView(R.id.iv_qr_code)
    ImageView mQrCode;
    @InjectView(R.id.ll_user_container)
    View mUserContainer;
    @InjectView(R.id.rl_user_unlogin)
    View mUserUnLogin;
    @InjectView(R.id.rootview)
    LinearLayout rootView;

    private static BadgeView mMesCount;

    private boolean mIsWatingLogin;

    private User mInfo;
    private AsyncTask<String, Void, User> mCacheTask;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_ACTION_LOGOUT)) {
                if (mErrorLayout != null) {
                    mIsWatingLogin = true;
                    steupUser();
                    mMesCount.hide();
                }
            } else if (action.equals(Constants.INTENT_ACTION_USER_CHANGE)) {
                requestData(true);
            } else if (action.equals(Constants.INTENT_ACTION_NOTICE)) {
                setNotice();
            }
        }
    };

    private final AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            try {
                mInfo = XmlUtils.toBean(MyInformation.class,
                        new ByteArrayInputStream(arg2)).getUser();
                if (mInfo != null) {
                    fillUI();
                    AppContext.getInstance().updateUserInfo(mInfo);
                    new SaveCacheTask(getActivity(), mInfo, getCacheKey())
                            .execute();
                } else {
                    onFailure(arg0, arg1, arg2, new Throwable());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(arg0, arg1, arg2, e);
            }
        }

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                Throwable arg3) {}
    };

    private void steupUser() {
        if (mIsWatingLogin) {
            mUserContainer.setVisibility(View.GONE);
            mUserUnLogin.setVisibility(View.VISIBLE);
        } else {
            mUserContainer.setVisibility(View.VISIBLE);
            mUserUnLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_ACTION_USER_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setNotice();
    }

    public void setNotice() {
        if (MainActivity.mNotice != null) {

            Notice notice = MainActivity.mNotice;
            int atmeCount = notice.getAtmeCount();// @我
            int msgCount = notice.getMsgCount();// 留言
            int reviewCount = notice.getReviewCount();// 评论
            int newFansCount = notice.getNewFansCount();// 新粉丝
            int newLikeCount = notice.getNewLikeCount();// 获得点赞
            int activeCount = atmeCount + reviewCount + msgCount + newFansCount + newLikeCount;// 信息总数
            if (activeCount > 0) {
                mMesCount.setText(activeCount + "");
                mMesCount.show();
            } else {
                mMesCount.hide();
            }

        } else {
            mMesCount.hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_information,
                container, false);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData(true);
        mInfo = AppContext.getInstance().getLoginUser();
        fillUI();
    }

    @Override
    public void initView(View view) {
        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        mIvAvatar.setOnClickListener(this);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().isLogin()) {
                    requestData(true);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
        view.findViewById(R.id.ly_favorite).setOnClickListener(this);
        view.findViewById(R.id.ly_following).setOnClickListener(this);
        view.findViewById(R.id.ly_follower).setOnClickListener(this);
        view.findViewById(R.id.rl_message).setOnClickListener(this);
        view.findViewById(R.id.rl_team).setOnClickListener(this);
        view.findViewById(R.id.rl_blog).setOnClickListener(this);
        view.findViewById(R.id.rl_note).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIHelper.showSimpleBack(getActivity(),
                                SimpleBackPage.NOTE);
                    }
                });
        mUserUnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLoginActivity(getActivity());
            }
        });

        mMesCount = new BadgeView(getActivity(), mMesView);
        mMesCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        mMesCount.setBadgePosition(BadgeView.POSITION_CENTER);
        mMesCount.setGravity(Gravity.CENTER);
        mMesCount.setBackgroundResource(R.drawable.notification_bg);
        mQrCode.setOnClickListener(this);
        // // 初始化团队列表数据
        // String cache = PreferenceHelper.readString(getActivity(),
        // TEAM_LIST_FILE, TEAM_LIST_KEY);
        // if (!StringUtils.isEmpty(cache)) {
        // List<Team> teams = TeamList.toTeamList(cache);
        // addTeamLayout(teams);
        // }
    }

    private void fillUI() {
        if (mInfo == null)
            return;
        mIvAvatar.setAvatarUrl(mInfo.getPortrait());
        mTvName.setText(mInfo.getName());
        mIvGender
                .setImageResource(StringUtils.toInt(mInfo.getGender()) != 2 ? R.drawable.userinfo_icon_male
                        : R.drawable.userinfo_icon_female);
        mTvScore.setText(String.valueOf(mInfo.getScore()));
        mTvFavorite.setText(String.valueOf(mInfo.getFavoritecount()));
        mTvFollowing.setText(String.valueOf(mInfo.getFollowers()));
        mTvFans.setText(String.valueOf(mInfo.getFans()));
    }

    private void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {
            mIsWatingLogin = false;
            String key = getCacheKey();
            if (refresh || TDevice.hasInternet()
                    && (!CacheManager.isExistDataCache(getActivity(), key))) {
                sendRequestData();
            } else {
                readCacheData(key);
            }
        } else {
            mIsWatingLogin = true;
        }
        steupUser();
    }

    private void readCacheData(String key) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(key);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private void sendRequestData() {
        int uid = AppContext.getInstance().getLoginUid();
        OSChinaApi.getMyInformation(uid, mHandler);
    }

    private String getCacheKey() {
        return "my_information" + AppContext.getInstance().getLoginUid();
    }

    private class CacheTask extends AsyncTask<String, Void, User> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected User doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return (User) seri;
            }
        }

        @Override
        protected void onPostExecute(User info) {
            super.onPostExecute(info);
            if (info != null) {
                mInfo = info;
                // mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                // } else {
                // mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                fillUI();
            }
        }
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (mIsWatingLogin) {
            AppContext.showToast(R.string.unlogin);
            UIHelper.showLoginActivity(getActivity());
            return;
        }
        final int id = v.getId();
        switch (id) {
        case R.id.iv_avatar:
            UIHelper.showSimpleBack(getActivity(),
                    SimpleBackPage.MY_INFORMATION_DETAIL);
            break;
        case R.id.iv_qr_code:
            showMyQrCode();
            break;
        case R.id.ly_following:
            UIHelper.showFriends(getActivity(), AppContext.getInstance()
                    .getLoginUid(), 0);
            break;
        case R.id.ly_follower:
            UIHelper.showFriends(getActivity(), AppContext.getInstance()
                    .getLoginUid(), 1);
            break;
        case R.id.ly_favorite:
            UIHelper.showUserFavorite(getActivity(), AppContext.getInstance()
                    .getLoginUid());
            break;
        case R.id.rl_message:
            UIHelper.showMyMes(getActivity());
            setNoticeReaded();
            break;
        case R.id.rl_team:
            UIHelper.showTeamMainActivity(getActivity());
            break;
        case R.id.rl_blog:
            UIHelper.showUserBlog(getActivity(), AppContext.getInstance()
                    .getLoginUid());
            break;
        case R.id.rl_user_center:
            UIHelper.showUserCenter(getActivity(), AppContext.getInstance()
                    .getLoginUid(), AppContext.getInstance().getLoginUser()
                    .getName());
            break;
        default:
            break;
        }
    }

    private void showMyQrCode() {
        MyQrodeDialog dialog = new MyQrodeDialog(getActivity());
        dialog.show();
    }

    @Override
    public void initData() {}

    private void setNoticeReaded() {
        mMesCount.setText("");
        mMesCount.hide();
    }

}
