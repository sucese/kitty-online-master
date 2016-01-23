package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册Fragment
 *
 * @author guoxiaoxing
 */
public class SigninFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.et_username)
    AppCompatEditText mEtUsername;
    @Bind(R.id.til_user_name)
    TextInputLayout mTilUserName;
    @Bind(R.id.et_verification)
    AppCompatEditText mEtVerification;
    @Bind(R.id.tv_get_verification)
    Button mBtnGetVerification;
    @Bind(R.id.til_verification)
    TextInputLayout mTilVerification;
    @Bind(R.id.et_password)
    AppCompatEditText mEtPassword;
    @Bind(R.id.til_password)
    TextInputLayout mTilPassword;
    @Bind(R.id.btn_signin)
    Button mBtnSignin;
    @Bind(R.id.tv_user_agreement)
    TextView mTvUserAgreement;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


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


    public void onInteraction(Uri uri) {
        if (mListener != null) {
            mListener.onSigninFragmentInteraction(uri);
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }


    public interface OnFragmentInteractionListener {
        void onSigninFragmentInteraction(Uri uri);
    }
}
