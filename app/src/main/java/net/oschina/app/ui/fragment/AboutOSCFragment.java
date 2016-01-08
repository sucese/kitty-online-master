package net.oschina.app.ui.fragment;

import net.oschina.app.R;
import net.oschina.app.base.BaseFragment;
import net.oschina.app.bean.SimpleBackPage;
import net.oschina.app.util.TDevice;
import net.oschina.app.util.UIHelper;
import net.oschina.app.util.UpdateManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutOSCFragment extends BaseFragment {

    @InjectView(R.id.tv_version)
    TextView mTvVersionStatus;

    @InjectView(R.id.tv_version_name)
    TextView mTvVersionName;

    @Override
    public View onCreateView(LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);
        view.findViewById(R.id.rl_feedback).setOnClickListener(this);
        view.findViewById(R.id.rl_grade).setOnClickListener(this);
        view.findViewById(R.id.rl_gitapp).setOnClickListener(this);
        view.findViewById(R.id.tv_oscsite).setOnClickListener(this);
        view.findViewById(R.id.tv_knowmore).setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvVersionName.setText("V " + TDevice.getVersionName());
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
        case R.id.rl_check_update:
            onClickUpdate();
            break;
        case R.id.rl_feedback:
            showFeedBack();
            break;
        case R.id.rl_grade:
            TDevice.openAppInMarket(getActivity());
            break;
        case R.id.rl_gitapp:
            boolean res = TDevice.openAppActivity(getActivity(),
                    "net.oschina.gitapp", "net.oschina.gitapp.WelcomePage");

            if (!res) {
                if (!TDevice.isHaveMarket(getActivity())) {
                    UIHelper.openSysBrowser(getActivity(),
                            "http://git.oschina.net/appclient");
                } else {
                    TDevice.gotoMarket(getActivity(), "net.oschina.gitapp");
                }
            }
            break;
        case R.id.tv_oscsite:
            UIHelper.openBrowser(getActivity(), "https://www.oschina.net");
            break;
        case R.id.tv_knowmore:
            UIHelper.openBrowser(getActivity(),
                    "https://www.oschina.net/home/aboutosc");
            break;
        default:
            break;
        }
    }

    private void onClickUpdate() {
        new UpdateManager(getActivity(), true).checkUpdate();
    }

    private void showFeedBack() {
        // TDevice.sendEmail(getActivity(), "用户反馈-OSC Android客户端", "",
        // "apposchina@163.com");
        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FEED_BACK);
    }
}
