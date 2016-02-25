package com.guoxiaoxing.kitty.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.api.remote.OSChinaApi;
import com.guoxiaoxing.kitty.cache.CacheManager;
import com.guoxiaoxing.kitty.model.Constants;
import com.guoxiaoxing.kitty.model.MyInformation;
import com.guoxiaoxing.kitty.model.Notice;
import com.guoxiaoxing.kitty.model.SimpleBackPage;
import com.guoxiaoxing.kitty.model.User;
import com.guoxiaoxing.kitty.ui.MainActivity;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.ui.dialog.MyQrodeDialog;
import com.guoxiaoxing.kitty.ui.empty.EmptyLayout;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TDevice;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.XmlUtils;
import com.guoxiaoxing.kitty.widget.AvatarView;
import com.guoxiaoxing.kitty.widget.BadgeView;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 登录用户中心页面
 *
 * @author guoxiaoxing
 */
public class MainMineFragment extends BaseFragment {

    public static final int sChildView = 9; // 在没有加入TeamList控件时rootview有多少子布局

    @Bind(R.id.iv_avatar)
    AvatarView mIvAvatar;
    @Bind(R.id.iv_gender)
    ImageView mIvGender;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_score)
    TextView mTvScore;
    @Bind(R.id.tv_favorite)
    TextView mTvFavorite;
    @Bind(R.id.tv_following)
    TextView mTvFollowing;
    @Bind(R.id.tv_follower)
    TextView mTvFans;

    @Bind(R.id.tv_mes)
    View mMesView;
    @Bind(R.id.error_layout)
    EmptyLayout mErrorLayout;
    @Bind(R.id.iv_qr_code)
    ImageView mQrCode;
    @Bind(R.id.ll_user_container)
    View mUserContainer;
    @Bind(R.id.rl_user_unlogin)
    View mUserUnLogin;

    private static BadgeView mMesCount;

    @Bind(R.id.ll_shop)
    LinearLayout mLlShop;
    @Bind(R.id.ll_wallet)
    LinearLayout mLlWallet;
    @Bind(R.id.ll_coupon)
    LinearLayout mLlCoupon;
    @Bind(R.id.ll_money)
    LinearLayout mLlMoney;
    @Bind(R.id.ll_super_shop)
    LinearLayout mLlSUperShop;

    @Bind(R.id.ll_card)
    LinearLayout mLlCard;
    @Bind(R.id.ll_note)
    LinearLayout mLlNote;
    @Bind(R.id.ll_account)
    LinearLayout mLlAccount;
    @Bind(R.id.ll_aunt)
    LinearLayout mLlAunt;
    @Bind(R.id.ll_star)
    LinearLayout mLlStar;
    @Bind(R.id.ll_food)
    LinearLayout mLlFood;
    @Bind(R.id.ll_album)
    LinearLayout mLlAlbum;
    @Bind(R.id.ll_service)
    LinearLayout mLlService;

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
                requestUserData(true);
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
                              Throwable arg3) {
        }
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
    protected int getLayoutId() {
        return R.layout.fragment_main_mine;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestUserData(true);
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
                    requestUserData(true);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
        view.findViewById(R.id.ly_favorite).setOnClickListener(this);
        view.findViewById(R.id.ly_following).setOnClickListener(this);
        view.findViewById(R.id.ly_follower).setOnClickListener(this);
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
        // 初始化团队列表数据
//         String cache = PreferenceHelper.readString(getActivity(),
//                 TEAM_LIST_FILE, TEAM_LIST_KEY);
//         if (!StringUtils.isEmpty(cache)) {
//         List<Team> teams = TeamList.toTeamList(cache);
//         addTeamLayout(teams);
//         }

        mLlShop.setOnClickListener(this);
        mLlWallet.setOnClickListener(this);
        mLlCoupon.setOnClickListener(this);
        mLlMoney.setOnClickListener(this);
        mLlSUperShop.setOnClickListener(this);
        mLlCard.setOnClickListener(this);
        mLlNote.setOnClickListener(this);
        mLlAccount.setOnClickListener(this);
        mLlAunt.setOnClickListener(this);
        mLlStar.setOnClickListener(this);
        mLlFood.setOnClickListener(this);
        mLlAlbum.setOnClickListener(this);
        mLlService.setOnClickListener(this);
    }

    private void fillUI() {
        if (mInfo == null)
            return;
        mIvAvatar.setAvatarUrl(mInfo.getFace());
        mTvName.setText(mInfo.getUsername());
        mIvGender
                .setImageResource(StringUtils.toInt(mInfo.getGender()) != 2 ? R.drawable.userinfo_icon_male
                        : R.drawable.userinfo_icon_female);
        mTvFollowing.setText(String.valueOf(mInfo.getFan()));
        mTvFans.setText(String.valueOf(mInfo.getAttention()));
    }

    private void requestUserData(boolean refresh) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                UIHelper.showSetting(getActivity());
//                showMyQrCode();
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

            case R.id.rl_user_center:
                UIHelper.showUserCenter(getActivity(), AppContext.getInstance()
                        .getLoginUid(), AppContext.getInstance().getLoginUser()
                        .getUsername());
                break;

            case R.id.ll_shop:
                break;
            case R.id.ll_wallet:
                break;
            case R.id.ll_coupon:
                break;
            case R.id.ll_money:
                break;
            case R.id.ll_super_shop:
                break;
            case R.id.ll_card:
                break;
            case R.id.ll_note:
                UIHelper.showSimpleBack(getActivity(),
                        SimpleBackPage.NOTE);
                break;
            case R.id.ll_account:
                break;
            case R.id.ll_aunt:
                break;
            case R.id.ll_star:
                break;
            case R.id.ll_food:
                break;
            case R.id.ll_album:
                break;
            case R.id.ll_service:
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
    public void initData() {
    }

    private void setNoticeReaded() {
        mMesCount.setText("");
        mMesCount.hide();
    }

}
