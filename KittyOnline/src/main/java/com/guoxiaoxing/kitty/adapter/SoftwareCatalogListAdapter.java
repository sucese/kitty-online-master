package com.guoxiaoxing.kitty.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoxiaoxing.kitty.R;
import com.guoxiaoxing.kitty.model.SoftwareCatalogList.SoftwareType;
import com.guoxiaoxing.kitty.ui.base.ListBaseAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SoftwareCatalogListAdapter extends ListBaseAdapter<SoftwareType> {
	
	static class ViewHold{
		@Bind(R.id.tv_software_catalog_name)TextView name;
		public ViewHold(View view) {
			ButterKnife.bind(this, view);
		}
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		
		ViewHold vh = null;
		
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_softwarecatalog, null);
			vh = new ViewHold(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHold)convertView.getTag();
		}
		
		SoftwareType softwareType = (SoftwareType) mDatas.get(position);
		vh.name.setText(softwareType.getName());
		return convertView;
		
	}
}
