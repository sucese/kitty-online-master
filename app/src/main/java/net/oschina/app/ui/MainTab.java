package net.oschina.app.ui;

import net.oschina.app.R;
import net.oschina.app.ui.fragment.ExploreFragment;
import net.oschina.app.ui.fragment.MyInformationFragment;
import net.oschina.app.viewpagerfragment.NewsViewPagerFragment;
import net.oschina.app.viewpagerfragment.TweetsViewPagerFragment;

/**
 * 主界面tab标签
 * @author guoxiaoxing
 */

public enum MainTab {

	NEWS(0, R.string.main_tab_name_news, R.drawable.sel_main_tab_home,
			NewsViewPagerFragment.class),

	TWEET(1, R.string.main_tab_name_tweet, R.drawable.sel_maini_tab_category,
			TweetsViewPagerFragment.class),

	QUICK(2, R.string.main_tab_name_quick, R.drawable.tab_icon_new,
			null),

	EXPLORE(3, R.string.main_tab_name_explore, R.drawable.sel_main_tab_shopping_cart,
			ExploreFragment.class),

	ME(4, R.string.main_tab_name_my, R.drawable.sel_main_tab_mine,
			MyInformationFragment.class);

	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
