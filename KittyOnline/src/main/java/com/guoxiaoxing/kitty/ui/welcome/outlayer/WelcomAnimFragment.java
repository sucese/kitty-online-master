package com.guoxiaoxing.kitty.ui.welcome.outlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.ImageFragmentStatePagerAdapter;
import com.guoxiaoxing.kitty.adapter.TextFragmentStatePagerAdapter;
import com.guoxiaoxing.kitty.model.TextBean;
import com.guoxiaoxing.kitty.ui.welcome.base.LoginAnimImageBaseFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.FristImageFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.SecondImageFragment;
import com.guoxiaoxing.kitty.ui.welcome.outlayer.welcomelayer.ThridImageFragment;
import com.guoxiaoxing.kitty.widget.ChildViewPager;
import com.guoxiaoxing.kitty.widget.WelcomeIndicator;

import java.util.ArrayList;


/**
 * 动画层
 */
public class WelcomAnimFragment extends Fragment {

    public ChildViewPager imageViewPager;
    public ChildViewPager textViewPager;
    public WelcomeIndicator view_indicator;
    public TextView tv_skip;
    public RelativeLayout rl_indicator;

    public int mOldPosition = -1;
    public boolean mIsMoveParent = false;
    private boolean mFristPageSuperLock = false;
    ImageFragmentStatePagerAdapter mImageFragmentStatePagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_welcomanim, null);

        imageViewPager = (ChildViewPager) view.findViewById(R.id.vp_imageanim);
        textViewPager = (ChildViewPager) view.findViewById(R.id.vp_textanim);
        view_indicator = (WelcomeIndicator) view.findViewById(R.id.view_indicator);
        tv_skip = (TextView) view.findViewById(R.id.tv_skip);
        rl_indicator = (RelativeLayout) view.findViewById(R.id.rl_indicator);

        initImageFragmentViewPager();
        initTextFragmentViewPager();
        return view;
    }


    private void initImageFragmentViewPager() {
        imageViewPager.setOffscreenPageLimit(3);
        mImageFragmentStatePagerAdapter = new ImageFragmentStatePagerAdapter(getChildFragmentManager());
        imageViewPager.setAdapter(mImageFragmentStatePagerAdapter);
        imageViewPager.mIsLockScoll = true;

        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == imageViewPager.getAdapter().getCount() - 1) {
                    mIsMoveParent = true;
                } else {
                    mIsMoveParent = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    /**
     * 初始化文字动画数据
     */
    private void initTextFragmentViewPager() {
        ArrayList<TextBean> mTextBeans = new ArrayList<>();
        TextBean bean0 = new TextBean();
        bean0.mId = 0;
        bean0.mTitle = "欢迎来到秀品!";
        bean0.mContent = "更酷更时尚的购物尽在秀品购物.";
        mTextBeans.add(bean0);

        TextBean bean1 = new TextBean();
        bean1.mId = 1;
        bean1.mTitle = "商品分类";
        bean1.mContent = "丰富多彩的商品，满足您的各种时尚需求.";
        mTextBeans.add(bean1);

        TextBean bean2 = new TextBean();
        bean2.mId = 2;
        bean2.mTitle = "推荐商品";
        bean2.mContent = "时尚大牌，品牌折扣，为你推荐.";
        mTextBeans.add(bean2);

        TextBean bean3 = new TextBean();
        bean3.mId = 3;
        bean3.mTitle = "风格商品";
        bean3.mContent = "各种风格商品标识，让你自由定位心仪商品.";
        mTextBeans.add(bean3);

        TextFragmentStatePagerAdapter adapterText = new TextFragmentStatePagerAdapter(getChildFragmentManager(), mTextBeans);
        textViewPager.setAdapter(adapterText);
        view_indicator.init(mTextBeans.size());

        /**
         * 文字动画层滑动监听
         */
        textViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int p) {
                if (mOldPosition < 0) {
                    mOldPosition = 0;
                }
                view_indicator.play(mOldPosition, p);

                /**
                 * 上层图片动画的viewpager有3个子fragment
                 * 下层文字动画的viewpager有4个子fragment
                 *        0   1   2  -->  图片动画
                 *   0   1   2   3  -->  文字动画
                 */
                if (p == 0) {
                    imageViewPager.mIsLockScoll = true;
                    if (mOldPosition == 1) {
                        mFristPageSuperLock = true;
                    }
                } else if (p == 1) {
                    imageViewPager.mIsLockScoll = false;
                    if (mFristPageSuperLock) {
                        mFristPageSuperLock = false;
                    } else {
                        reset();
                        FristImageFragment fragment = (FristImageFragment) mImageFragmentStatePagerAdapter.getFragement(0);
                        fragment.playInAnim();
                    }
                } else if (p == 2) {
                    imageViewPager.mIsLockScoll = false;
                    reset();
                    SecondImageFragment fragment = (SecondImageFragment) mImageFragmentStatePagerAdapter.getFragement(1);
                    fragment.playInAnim();
                } else if (p == 3) {
                    imageViewPager.mIsLockScoll = false;
                    reset();
                    ThridImageFragment fragment = (ThridImageFragment) mImageFragmentStatePagerAdapter.getFragement(2);
                    fragment.playInAnim();
                } else {
                    imageViewPager.mIsLockScoll = false;
                    reset();
                }
                mOldPosition = p;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    /**
     * 非当前展示动画页,停止动画,回复初始状态
     */
    public void reset() {
        if (mOldPosition > 0) {
            LoginAnimImageBaseFragment fragment = (LoginAnimImageBaseFragment) mImageFragmentStatePagerAdapter.getFragement(mOldPosition - 1);
            fragment.reset();
        }
    }

    /**
     * 跳过监听
     */
    public WelcomAnimFragmentInterface mWelcomAnimFragmentInterface;

    public void setWelcomAnimFragmentInterface(WelcomAnimFragmentInterface mInterface) {
        this.mWelcomAnimFragmentInterface = mInterface;
    }

    public interface WelcomAnimFragmentInterface {
        void onSkip();
    }
}
