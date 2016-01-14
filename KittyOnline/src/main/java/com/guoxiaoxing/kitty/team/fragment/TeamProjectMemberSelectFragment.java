package com.guoxiaoxing.kitty.team.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.guoxiaoxing.kitty.ui.base.BaseActivity;

/**
 *
 * @author guoxiaoxing
 */
public class TeamProjectMemberSelectFragment extends TeamProjectMemberFragment {
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String title = getArguments().getString("title");
            if (TextUtils.isEmpty(title)) {
        	((BaseActivity)getActivity()).setActionBarTitle("添加");
            } else {
        	((BaseActivity)getActivity()).setActionBarTitle(title);
            }
        }
        
    }
}

