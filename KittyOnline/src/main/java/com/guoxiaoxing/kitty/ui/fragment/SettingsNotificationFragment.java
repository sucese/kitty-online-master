package com.guoxiaoxing.kitty.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guoxiaoxing.kitty.AppConfig;
import com.guoxiaoxing.kitty.AppContext;
import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.ui.base.BaseFragment;
import com.guoxiaoxing.kitty.widget.togglebutton.ToggleButton;
import com.guoxiaoxing.kitty.widget.togglebutton.ToggleButton.OnToggleChanged;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsNotificationFragment extends BaseFragment {
	
	@Bind(R.id.tb_accept) ToggleButton mTbAccept;
	@Bind(R.id.tb_voice) ToggleButton mTbVoice;
	@Bind(R.id.tb_vibration) ToggleButton mTbVibration;
	@Bind(R.id.tb_app_exit) ToggleButton mTbAppExit;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings_notifcation, container,
				false);
		ButterKnife.bind(this, view);
		initView(view);
		initData();
		return view;
	}
	
	@Override
	public void initView(View view) {
		setToggleChanged(mTbAccept, AppConfig.KEY_NOTIFICATION_ACCEPT);
		setToggleChanged(mTbVoice, AppConfig.KEY_NOTIFICATION_SOUND);
		setToggleChanged(mTbVibration, AppConfig.KEY_NOTIFICATION_VIBRATION);
		setToggleChanged(mTbAppExit, AppConfig.KEY_NOTIFICATION_DISABLE_WHEN_EXIT);
		
		view.findViewById(R.id.rl_accept).setOnClickListener(this);
		view.findViewById(R.id.rl_voice).setOnClickListener(this);
		view.findViewById(R.id.rl_vibration).setOnClickListener(this);
		view.findViewById(R.id.rl_app_exit).setOnClickListener(this);
	}

	public void initData() {
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_ACCEPT, true), mTbAccept);
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_SOUND, true), mTbVoice);
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_VIBRATION, true), mTbVibration);
		setToggle(AppContext.get(AppConfig.KEY_NOTIFICATION_DISABLE_WHEN_EXIT, true), mTbAppExit);
	}
	
	private void setToggleChanged(ToggleButton tb, final String key) {
		tb.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				AppContext.set(key, on);
			}
		});
	}
	
	private void setToggle(boolean value, ToggleButton tb) {
		if (value)
			tb.setToggleOn();
		else
			tb.setToggleOff();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		switch (id) {
		case R.id.rl_accept:
			mTbAccept.toggle();
			break;
		case R.id.rl_voice:
			mTbVoice.toggle();
			break;
		case R.id.rl_vibration:
			mTbVibration.toggle();
			break;
		case R.id.rl_app_exit:
			mTbAppExit.toggle();
			break;
		}
	}
}
