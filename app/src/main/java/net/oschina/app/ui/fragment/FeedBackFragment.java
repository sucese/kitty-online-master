package net.oschina.app.ui.fragment;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.api.remote.OSChinaApi;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.util.StringUtils;
import net.oschina.app.util.TDevice;

import cz.msebera.android.httpclient.Header;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class FeedBackFragment extends BaseFragment {
    @InjectView(R.id.et_feedback)
    EditText mEtContent;
    @InjectView(R.id.et_contact)
    EditText mEtContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feedback, null);
        ButterKnife.inject(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.submit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.public_menu_send:
            String data = mEtContent.getText().toString();
            if (StringUtils.isEmpty(data)) {
                AppContext.showToast("你忘记写建议咯");
            } else {
                data += "<br>";
                data += mEtContact.getText() + "<br>";
                data += TDevice.getVersionName() + "("
                        + TDevice.getVersionCode() + ")<br>";
                OSChinaApi.feedback(data, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        AppContext.showToast("已收到你的建议，谢谢");
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                            Throwable arg3) {
                        AppContext.showToast("网络异常，请稍后重试");
                    }
                });
            }
            break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }
}
