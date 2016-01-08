package net.oschina.app.team.fragment;

import net.oschina.app.base.BaseActivity;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * TeamMemberSelectFragment.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @data 2015-3-10 下午3:54:40
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

