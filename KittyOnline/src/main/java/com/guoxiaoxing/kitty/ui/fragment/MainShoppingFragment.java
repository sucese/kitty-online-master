package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.AdvertisementBanner;
import com.guoxiaoxing.kitty.model.SimpleBackPage;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.widget.banner.ConvenientBanner;
import com.guoxiaoxing.kitty.widget.banner.holder.CBViewHolderCreator;
import com.guoxiaoxing.kitty.widget.banner.holder.NetworkImageHolderView;
import com.guoxiaoxing.kitty.widget.banner.listener.OnItemClickListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class MainShoppingFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainShoppingFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context mContext;
    private String mParam1;
    private String mParam2;

    //    @Bind(R.id.srl_root)
//    SwipeRefreshLayout mSrlRoot;
    @Bind(R.id.tb_main_shopping_fragment)
    Toolbar mToolbar;
    @Bind(R.id.iv_scan)
    ImageView mIvScan;
    @Bind(R.id.iv_notification)
    ImageView mIvNotification;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.cb_shopping)
    ConvenientBanner mConvenientBanner;
    @Bind(R.id.ctl_layout)
    CollapsingToolbarLayout mCtlLayout;
    @Bind(R.id.tb_content)
    TabLayout mTbContent;
    @Bind(R.id.vp_title)
    ViewPager mVpTitle;

    @Bind(R.id.sdv_first)
    SimpleDraweeView mSdvFirst;
    @Bind(R.id.sdv_second)
    SimpleDraweeView mSdvSecond;
    @Bind(R.id.sdv_three)
    SimpleDraweeView mSdvThree;

    private OnFragmentInteractionListener mListener;
    LinearLayoutManager mLayoutManager;
    int moreNum = 2;
    boolean isDrag = true;

    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private View headerView;

    private String[] images = {
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_0.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_1.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_2.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_3.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bannermain_banner_4.jpg"};

    private String[] bazaar = {
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_2.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_3.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_4.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_5.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_6.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_0.jpg",
            "http://7xq26k.com1.z0.glb.clouddn.com/bazaar_1.jpg"
    };

    public MainShoppingFragment() {
    }

    public static MainShoppingFragment newInstance(String param1, String param2) {
        MainShoppingFragment fragment = new MainShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(AppConfig.VIEWPAGER_TRANSFORM_TIME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void onClick(View v) {

        super.onClick(v);

        switch (v.getId()) {
            //消息通知
            case R.id.iv_notification:
                UIHelper.showMyMes(getActivity());
                break;
            //搜索框
            case R.id.et_search:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SEARCH);
                break;
            case R.id.iv_scan:
                UIHelper.showScanActivity(getActivity());
                break;
            default:
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_shopping;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        initToolbar();
        initContentView();
    }

    @Override
    public void initData() {

        //查询广告Banner信息
        AVQuery<AdvertisementBanner> query = AVQuery.getQuery(AdvertisementBanner.class);
        query.findInBackground(new FindCallback<AdvertisementBanner>() {
            @Override
            public void done(List<AdvertisementBanner> list, AVException e) {
                if (e == null) {
                    // 网络加载例子
                    ArrayList<String> networkImages = new ArrayList<String>();

                    for (int i = 0; i < list.size(); i++) {
                        networkImages.add(list.get(i).getImageUrl());
                    }
                    mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                        @Override
                        public NetworkImageHolderView createHolder() {
                            return new NetworkImageHolderView();
                        }
                    }, networkImages)                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                            .setPageIndicator(new int[]{R.drawable.shp_cycle_white, R.drawable.shp_circle_pink})
//                .setOnPageChangeListener(this)//监听翻页事件
//                            .setOnItemClickListener(this);
                    ;
                } else {

                }
            }
        });


    }

    @Override
    public void setToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHomeFragmentInteraction(uri);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                mSrlRoot.setRefreshing(false);
            }
        }, 3000);
    }

    public interface OnFragmentInteractionListener {
        void onHomeFragmentInteraction(Uri uri);
    }

    public int getScreenHeight() {
        return getActivity().findViewById(android.R.id.content).getHeight();
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void initToolbar() {
        mContext = getActivity();
        mEtSearch.setFocusable(false);
        mIvScan.setOnClickListener(this);
        mIvNotification.setOnClickListener(this);
        mEtSearch.setOnClickListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initContentView() {


        mSdvFirst.setImageURI(Uri.parse(bazaar[0]));
        mSdvSecond.setImageURI(Uri.parse(bazaar[1]));
        mSdvThree.setImageURI(Uri.parse(bazaar[2]));

        setupViewPager(mVpTitle);

//给TabLayout增加Tab, 并关联ViewPager
        mTbContent.addTab(mTbContent.newTab().setText("关注"));
        mTbContent.addTab(mTbContent.newTab().setText("热门"));
        mTbContent.addTab(mTbContent.newTab().setText("我配"));
        mTbContent.addTab(mTbContent.newTab().setText("私搭"));
        mTbContent.addTab(mTbContent.newTab().setText("晒货"));
        mTbContent.addTab(mTbContent.newTab().setText("星榜"));
        mTbContent.addTab(mTbContent.newTab().setText("妆呗"));
        mTbContent.addTab(mTbContent.newTab().setText("男票"));
        mTbContent.addTab(mTbContent.newTab().setText("好吃"));
        mTbContent.addTab(mTbContent.newTab().setText("脸赞"));
        mTbContent.setupWithViewPager(mVpTitle);
    }

    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(TalkFragment.newInstance(""), "关注");
        adapter.addFragment(TalkFragment.newInstance(""), "热门");
        adapter.addFragment(TalkFragment.newInstance(""), "我配");
        adapter.addFragment(TalkFragment.newInstance(""), "私搭");
        adapter.addFragment(TalkFragment.newInstance(""), "晒货");
        adapter.addFragment(TalkFragment.newInstance(""), "星榜");
        adapter.addFragment(TalkFragment.newInstance(""), "妆呗");
        adapter.addFragment(TalkFragment.newInstance(""), "男票");
        adapter.addFragment(TalkFragment.newInstance(""), "好吃");
        adapter.addFragment(TalkFragment.newInstance(""), "脸赞");
        mViewPager.setAdapter(adapter);
    }


    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}