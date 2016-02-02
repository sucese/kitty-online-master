package com.guoxiaoxing.kitty.ui;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.ui.fragment.MainChatFragment;
import com.guoxiaoxing.kitty.ui.fragment.MainShoppingFragment;
import com.guoxiaoxing.kitty.ui.fragment.MainMineFragment;
import com.guoxiaoxing.kitty.ui.fragment.MainBuyFragment;

/**
 * 主界面tab标签
 * @author guoxiaoxing
 */

public enum MainTab {

	SHOPPING(0, R.string.main_tab_name_shoppoing, R.drawable.sel_main_tab_home,
			MainShoppingFragment.class),

	BUY(1, R.string.main_tab_name_buy, R.drawable.sel_main_tab_buy,
			MainBuyFragment.class),

	QUICK(2, R.string.main_tab_name_quick, R.drawable.tab_icon_new,
			null),

	CHAT(3, R.string.main_tab_name_chat, R.drawable.sel_main_tab_chat,
			MainChatFragment.class),

	MINE(4, R.string.main_tab_name_mine, R.drawable.sel_main_tab_mine,
			MainMineFragment.class);

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
