package com.guoxiaoxing.kitty.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.adapter.MainBuyAdapter;
import com.guoxiaoxing.kitty.model.SimpleBackPage;
import com.guoxiaoxing.kitty.tmp.SampleDataboxset;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.util.UIHelper;
import com.guoxiaoxing.kitty.util.log.Logger;
import com.guoxiaoxing.kitty.widget.banner.ConvenientBanner;
import com.guoxiaoxing.kitty.widget.banner.holder.CBViewHolderCreator;
import com.guoxiaoxing.kitty.widget.banner.holder.LocalImageHolderView;
import com.guoxiaoxing.kitty.widget.banner.listener.OnItemClickListener;
import com.guoxiaoxing.kitty.widget.banner.transforms.FlipHorizontalTransformer;
import com.guoxiaoxing.kitty.widget.timecounter.CountdownView;
import com.guoxiaoxingv.smartrecyclerview.ItemTouchListenerAdapter;
import com.guoxiaoxingv.smartrecyclerview.ObservableScrollState;
import com.guoxiaoxingv.smartrecyclerview.ObservableScrollViewCallbacks;
import com.guoxiaoxingv.smartrecyclerview.SmartRecyclerView;
import com.guoxiaoxingv.smartrecyclerview.animator.animators.FadeInAnimator;
import com.guoxiaoxingv.smartrecyclerview.util.BasicGridLayoutManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.Bind;


public class MainBuyFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        ViewPager.OnPageChangeListener, OnItemClickListener {

    private static final String TAG = "MainShoppingFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext;
    private String mParam1;
    private String mParam2;

    @Bind(R.id.tb_main_buy_fragment)
    Toolbar mToolbar;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.srv_main_buy_fragment)
    SmartRecyclerView mSmartRecyclerView;


    ConvenientBanner mConvenientBanner;
    CountdownView mCountdownView;

    private OnFragmentInteractionListener mListener;
    MainBuyAdapter mAdapter = null;
    GridLayoutManager mLayoutManager;

    int moreNum = 2;
    boolean isDrag = true;

    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private View headerView;

    public MainBuyFragment() {
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
        Logger.t(TAG).d("onCreate()");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.t(TAG).d("onAttach()");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.t(TAG).d("onViewCreated()");

    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.t(TAG).d("onResume()");
        mConvenientBanner.startTurning(AppConfig.VIEWPAGER_TRANSFORM_TIME);
        long time2 = (long) 30 * 60 * 1000;
        mCountdownView.start(time2);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.t(TAG).d("onPause()");
        mConvenientBanner.stopTurning();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.t(TAG).d("onDestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.t(TAG).d("onDetach()");
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
            //搜索框
            case R.id.et_search:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SEARCH);
                break;
            default:
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_buy;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        Logger.t(TAG).d("onCreateView() -- initView()");
        initToolbar();
        initContentView();
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHomeFragmentInteraction(uri);
        }
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
        mEtSearch.setOnClickListener(this);
    }

    private void initHeaderView() {

        //设置HeaderView
        headerView = LayoutInflater.from(mContext).inflate(R.layout.buy_recyclerview_header, mSmartRecyclerView.mRecyclerView, false);
        mCountdownView = (CountdownView) headerView.findViewById(R.id.cv_sale);


        for (int position = 0; position < 7; position++) {
            localImages.add(getResId("ic_buy_banner_" + position, R.drawable.class));
        }


        mConvenientBanner = (ConvenientBanner) headerView.findViewById(R.id.cb_sale_ad);
        mConvenientBanner.getViewPager().setPageTransformer(true, new FlipHorizontalTransformer());
        //本地图片例子
        mConvenientBanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
//                .setOnPageChangeListener(this)//监听翻页事件
                .setOnItemClickListener(this);

//        mCbHomeAd.setManualPageable(false);//设置不能手动影响


    }

    private void initContentView() {

        mSmartRecyclerView.setHasFixedSize(false);
        mAdapter = new MainBuyAdapter(SampleDataboxset.newList());
        mLayoutManager = new BasicGridLayoutManager(mContext, 2, mAdapter);
        mSmartRecyclerView.setLayoutManager(mLayoutManager);
        mSmartRecyclerView.setAdapter(mAdapter);


        mSmartRecyclerView.setItemAnimator(new FadeInAnimator());
        mSmartRecyclerView.getItemAnimator().setAddDuration(300);
        mSmartRecyclerView.getItemAnimator().setRemoveDuration(300);

        //加载更多
        mSmartRecyclerView.enableLoadmore();
        mAdapter.setCustomLoadMoreView(LayoutInflater.from(mContext)
                .inflate(R.layout.custom_bottom_progressbar, null, false));

        //添加Header View
        initHeaderView();
        mSmartRecyclerView.setNormalHeader(headerView);
        mSmartRecyclerView.setOnParallaxScroll(new SmartRecyclerView.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
//                Drawable drawable = mToolbar.getBackground();
//                drawable.setAlpha(Math.round(127 + percentage * 128));
//                mToolbar.setBackground(drawable);
            }
        });

        //下拉刷新数据
        mSmartRecyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.insert(moreNum++ + "  Refresh things", 0);
                        mSmartRecyclerView.setRefreshing(false);
                        //   ultimateRecyclerView.scrollBy(0, -50);
                        mLayoutManager.scrollToPosition(0);
//                        ultimateRecyclerView.setAdapter(mAdapter);
//                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        //上拉加载更多
        mSmartRecyclerView.setOnLoadMoreListener(new SmartRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        SampleDataboxset.insertMore(mAdapter, 10);
                        //  mLayoutManager.scrollToPositionWithOffset(maxLastVisiblePosition, -1);
                        //  mLayoutManager.scrollToPosition(maxLastVisiblePosition);
                    }
                }, 2500);
            }
        });

        //滑动监听，可以根据不同的状态来显示和隐藏Toolbar和Floating button
        mSmartRecyclerView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

                Logger.t(TAG).d("onDownMotionEvent" + dragging);
            }

            @Override
            public void onDownMotionEvent() {
                Logger.t(TAG).d("onDownMotionEvent");
            }

            @Override
            public void onUpOrCancelMotionEvent(ObservableScrollState observableScrollState) {
                Logger.t(TAG).d("onUpOrCancelMotionEvent");
                if (observableScrollState == ObservableScrollState.UP) {
                    mSmartRecyclerView.hideToolbar(mToolbar, mSmartRecyclerView, getScreenHeight());
                    mSmartRecyclerView.hideFloatingActionMenu();
                } else if (observableScrollState == ObservableScrollState.DOWN) {
                    mSmartRecyclerView.showToolbar(mToolbar, mSmartRecyclerView, getScreenHeight());
                    mSmartRecyclerView.showFloatingActionMenu();
                }
            }
        });

        ItemTouchListenerAdapter itemTouchListenerAdapter = new ItemTouchListenerAdapter(mSmartRecyclerView.mRecyclerView,
                new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView parent, View clickedView, int position) {
                    }

                    @Override
                    public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
                        if (isDrag) {

                        }

                    }
                });
        mSmartRecyclerView.mRecyclerView.addOnItemTouchListener(itemTouchListenerAdapter);
    }

}