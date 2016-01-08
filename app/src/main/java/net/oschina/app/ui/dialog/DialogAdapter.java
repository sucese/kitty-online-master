package net.oschina.app.ui.dialog;

import net.oschina.app.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class DialogAdapter extends BaseAdapter {

	private CharSequence[] _items;
	private int select;
	private boolean showChk = true;
	
	public DialogAdapter(CharSequence[] items, int selectIdx) {
		_items = items;
		this.select = selectIdx;
	}
	
	public DialogAdapter(CharSequence[] items) {
		_items = items;
	}

	@Override
	public int getCount() {
		return _items.length;
	}

	@Override
	public String getItem(int i) {
		return _items[i].toString();// super.getItem(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewgroup) {
		DialogHolder vh = null;
		if (view == null) {
			vh = new DialogHolder();
			view = LayoutInflater.from(viewgroup.getContext()).inflate(
					R.layout.list_cell_dialog, null, false);
			vh.titleTv = (TextView) view.findViewById(R.id.title_tv);
			vh.divider = view.findViewById(R.id.list_divider);
			vh.checkIv = (RadioButton) view.findViewById(R.id.rb_select);
			view.setTag(vh);
		} else {
			vh = (DialogHolder) view.getTag();
		}
		vh.titleTv.setText(getItem(i));
		
		if (i == -1 + getCount()) {
			vh.divider.setVisibility(View.GONE);
		} else {
			vh.divider.setVisibility(View.VISIBLE);
		}
		
		if(showChk) {
			vh.checkIv.setVisibility(View.VISIBLE);
			if (select == i) {
				vh.checkIv.setChecked(true);
			} else {
				vh.checkIv.setChecked(false);
			}
		} else{
			vh.checkIv.setVisibility(View.GONE);
		}
		return view;
	}

	public boolean isShowChk() {
		return showChk;
	}

	public void setShowChk(boolean showChk) {
		this.showChk = showChk;
	}

	private class DialogHolder {
		public View divider;
		public TextView titleTv;
		public RadioButton checkIv;
	}
}
