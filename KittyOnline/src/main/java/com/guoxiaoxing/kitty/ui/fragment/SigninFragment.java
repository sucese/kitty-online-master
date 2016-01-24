package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.SnackBarHelper;
import com.guoxiaoxing.kitty.util.StringUtils;
import com.guoxiaoxing.kitty.util.TDevice;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册Fragment
 *
 * @author guoxiaoxing
 */
public class SigninFragment extends BaseFragment implements TextWatcher {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.ll_root)
    LinearLayout mLlRoot;

    @Bind(R.id.til_user_phone)
    TextInputLayout mTilUserName;
    @Bind(R.id.et_user_phone)
    AppCompatEditText mEtUserPhone;
    @Bind(R.id.til_verification)
    TextInputLayout mTilVerification;
    @Bind(R.id.et_verification)
    AppCompatEditText mEtVerification;
    @Bind(R.id.btn_get_verification)
    Button mBtnGetVerification;
    @Bind(R.id.til_user_password)
    TextInputLayout mTilUserPassword;
    @Bind(R.id.et_user_password)
    AppCompatEditText mEtUserPassword;
    @Bind(R.id.btn_signin)
    Button mBtnSignin;
    @Bind(R.id.tv_user_agreement)
    TextView mTvUserAgreement;

    private Context mContext;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String mUserPhone;
    private String mVerificationCode;
    private String mUserPassword;


    public SigninFragment() {
    }


    public static SigninFragment newInstance(String param1, String param2) {
        SigninFragment fragment = new SigninFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signin;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = getActivity();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onPause() {
        super.onPause();
        onInteraction(null);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        int id = v.getId();
        switch (id) {
            case R.id.btn_get_verification:
                handleGetVerification();
                break;
            case R.id.btn_signin:
                handleSignin();
                break;
        }

    }

    public void onInteraction(Uri uri) {
        if (mListener != null) {
            mListener.onSigninFragmentInteraction(uri);
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mBtnGetVerification.setOnClickListener(this);
        mBtnSignin.setOnClickListener(this);
        mEtUserPhone.addTextChangedListener(this);
        mEtVerification.addTextChangedListener(this);
        mEtUserPassword.addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getSigninInfo();

        if (!StringUtils.isEmpty(mUserPhone) && !StringUtils.isEmpty(mVerificationCode) && !StringUtils.isEmpty(mUserPassword)) {
            mBtnSignin.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rip_btn_pink));
        } else {
            mBtnSignin.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray_300));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnFragmentInteractionListener {
        void onSigninFragmentInteraction(Uri uri);
    }

    /**
     * 获取验证码
     */
    private void handleGetVerification() {

        getSigninInfo();
        if (StringUtils.isEmpty(mUserPhone)) {
            mTilUserName.setError("请输入手机号");
            mEtUserPhone.requestFocus();
            return;
        }

        //获取手机验证码
        AVUser.requestMobilePhoneVerifyInBackground(mUserPhone, new RequestMobileCodeCallback() {

            @Override
            public void done(AVException e) {
                if (e == null) {
                    SnackBarHelper.showSnackBar(mLlRoot, "验证码已发送，请输入验证码");
                    mBtnSignin.setText("60秒后重新发送");
                } else {
                    SnackBarHelper.showSnackBar(mLlRoot, "验证码已发送，请输入验证码");
                }
            }
        });

    }

    /**
     * 注册
     */
    private void handleSignin() {

        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return;
        }

        getSigninInfo();
        if (StringUtils.isEmpty(mUserPhone)) {
            mTilUserName.setError("请输入手机号");
            mEtUserPhone.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(mVerificationCode)) {
            mTilVerification.setError("请输入验证码");
            mEtVerification.requestFocus();
            return;
        }
        if (StringUtils.isEmpty(mUserPassword)) {
            mTilUserPassword.setError("请输入密码");
            mEtUserPassword.requestFocus();
            return;
        }

        AVUser user = new AVUser();
        user.setUsername(mUserPhone);
        user.setPassword(mUserPassword);
        user.setMobilePhoneNumber(mUserPhone);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {

                if (e == null) {
                    SnackBarHelper.showSnackBar(mLlRoot, "注册成功");
                } else {
                    SnackBarHelper.showSnackBar(mLlRoot, "注册失败" + e.getMessage());
                }

            }
        });
    }

    /**
     * 获取用户输入的注册信息
     */
    private void getSigninInfo() {
        mUserPhone = mEtUserPhone.getText().toString().trim();
        mVerificationCode = mEtVerification.getText().toString().trim();
        mUserPassword = mEtUserPassword.getText().toString().trim();
    }
}
