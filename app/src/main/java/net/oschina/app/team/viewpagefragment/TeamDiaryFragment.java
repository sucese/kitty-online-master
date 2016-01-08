package net.oschina.app.team.viewpagefragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.cache.CacheManager;
import net.oschina.app.team.adapter.TeamDiaryPagerAdapter;
import net.oschina.app.team.bean.Team;
import net.oschina.app.team.bean.TeamDiaryList;
import net.oschina.app.team.ui.TeamMainActivity;
import net.oschina.app.util.StringUtils;

import org.kymjs.kjframe.http.KJAsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

public class TeamDiaryFragment extends BaseFragment implements
        OnDateSetListener {
    private static String TAG = "TeamDiaryPagerFragment";
    public static String DIARYDETAIL_KEY = "team_diary_detail_key";
    public static String TEAMID_KEY = "team_diary_teamid_key";

    @InjectView(R.id.team_diary_pager)
    ViewPager mPager;
    @InjectView(R.id.team_diary_pager_title)
    TextView mTvTitle;
    @InjectView(R.id.team_diary_pager_calendar)
    ImageView mImgCalendar;
    @InjectView(R.id.team_diary_pager_left)
    ImageView mImgLeft;
    @InjectView(R.id.team_diary_pager_right)
    ImageView mImgRight;

    private Activity aty;
    private Team team;
    private int currentWeek;
    private int currentYear = 2015;
    private Map<Integer, TeamDiaryList> dataBundleList; // 用于实现二级缓存
    private final Calendar calendar = Calendar.getInstance();
    private TeamDiaryPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        aty = getActivity();
        View view = View.inflate(aty, R.layout.fragment_team_diarypager, null);
        ButterKnife.inject(this, view);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            team = (Team) bundle
                    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
        }
        TAG += team.getId();
        currentWeek = StringUtils.getWeekOfYear() - 1;
        dataBundleList = new HashMap<Integer, TeamDiaryList>(currentWeek + 5);
        // 异步读缓存
        KJAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < currentWeek; i++) {
                    TeamDiaryList dataBundle = ((TeamDiaryList) CacheManager
                            .readObject(aty, TAG + i));
                    if (dataBundle != null) {
                        if (dataBundleList.get(i) != null) {
                            dataBundleList.remove(i);
                        }
                        dataBundleList.put(i, dataBundle);
                    }
                }
            }
        });
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        adapter = new TeamDiaryPagerAdapter(aty, currentYear, team.getId());
        mPager.setAdapter(adapter);
        changeUI(currentWeek, adapter.getCount());
        mImgCalendar.setOnClickListener(this);
        mImgLeft.setOnClickListener(this);
        mImgRight.setOnClickListener(this);
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                changeUI(arg0, mPager.getAdapter().getCount());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }

    /**
     * 改变Title
     * 
     * @param currentPage
     *            当前页(从0开始)
     * @param totalPage
     *            总共有多少页(从0开始)
     */
    private void changeUI(int currentPage, int totalPage) {
        mPager.setCurrentItem(currentPage);

        if (currentPage <= 0) {
            mImgLeft.setImageResource(R.drawable.ic_diary_back);
        } else {
            mImgLeft.setImageResource(R.drawable.ic_diary_canback);
        }
        if (currentPage >= adapter.getCount() - 1) {
            mImgRight.setImageResource(R.drawable.ic_diary_forward);
        } else {
            mImgRight.setImageResource(R.drawable.ic_diary_canforward);
        }
        mTvTitle.setText("第" + (currentPage + 1) + "周周报总览");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.team_diary_pager_right:
            int currentPage1 = mPager.getCurrentItem();
            if (currentPage1 < mPager.getAdapter().getCount()) {
                mPager.setCurrentItem(currentPage1 + 1);
            }
            break;
        case R.id.team_diary_pager_left:
            int currentPage2 = mPager.getCurrentItem();
            if (currentPage2 > 0) {
                mPager.setCurrentItem(currentPage2 - 1);
            }
            break;
        case R.id.team_diary_pager_calendar:
            final DatePickerDialog datePickerDialog = DatePickerDialog
                    .newInstance(this, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH), false);
            datePickerDialog.setVibrate(false);
            datePickerDialog.setYearRange(2014, 2015);
            datePickerDialog.show(getFragmentManager(), "datepicker");
            break;
        default:
            break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year,
            int month, int day) {
        int[] dateBundle = StringUtils.getCurrentDate();
        if ((dateBundle[0] == year && dateBundle[1] <= month)
                || (dateBundle[0] == year && dateBundle[1] == month + 1 && dateBundle[2] < day)) {
            AppContext.showToast("那天怎么会有周报呢");
        } else {
            currentYear = year;
            currentWeek = StringUtils.getWeekOfYear(new Date(year, month, day)) - 1;
            mPager.setAdapter(new TeamDiaryPagerAdapter(aty, year, team.getId()));
            changeUI(currentWeek, mPager.getAdapter().getCount());
        }
    }
}
