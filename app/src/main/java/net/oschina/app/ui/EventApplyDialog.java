package net.oschina.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.bean.Event;
import net.oschina.app.bean.EventApplyData;
import net.oschina.app.ui.dialog.CommonDialog;
import net.oschina.app.util.DialogHelp;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EventApplyDialog extends CommonDialog implements
        android.view.View.OnClickListener {

    @InjectView(R.id.et_name)
    EditText mName;

    @InjectView(R.id.tv_gender)
    TextView mGender;

    private String[] genders;

    @InjectView(R.id.et_phone)
    EditText mMobile;

    @InjectView(R.id.et_company)
    EditText mCompany;

    @InjectView(R.id.et_job)
    EditText mJob;

    @InjectView(R.id.tv_remarks_tip)
    TextView mTvRemarksTip;// 备注提示

    @InjectView(R.id.tv_remarks_selecte)
    TextView mTvRemarksSelected;// 备注选择

    private Event mEvent;

    private EventApplyDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private EventApplyDialog(Context context, int defStyle, Event event) {
        super(context, defStyle);
        View shareView = getLayoutInflater().inflate(
                R.layout.dialog_event_apply, null);
        ButterKnife.inject(this, shareView);
        setContent(shareView, 0);
        this.mEvent = event;
        initView();
    }

    private void initView() {
        genders = getContext().getResources().getStringArray(
                R.array.gender);

        mGender.setText(genders[0]);

        mGender.setOnClickListener(this);


        if (mEvent.getEventRemark() != null) {
            mTvRemarksTip.setVisibility(View.VISIBLE);
            mTvRemarksTip.setText(mEvent.getEventRemark().getRemarkTip());
            mTvRemarksSelected.setVisibility(View.VISIBLE);

            mTvRemarksSelected.setOnClickListener(this);

            mTvRemarksSelected.setText(mEvent.getEventRemark().getSelect().getList().get(0));
        }
    }

    public EventApplyDialog(Context context, Event event) {
        this(context, R.style.dialog_bottom, event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_gender:
                selectGender();
                break;
            case R.id.tv_remarks_selecte:
                selectRemarkSelect();
                break;
            default:
                break;
        }
    }

    private void selectGender() {
        String gender = mGender.getText().toString();
        int idx = 0;
        for (int i = 0; i < genders.length; i++) {
            if (genders[i].equals(gender)) {
                idx = i;
                break;
            }
        }
        DialogHelp.getSelectDialog(getContext(), genders, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mGender.setText(genders[i]);
            }
        }).show();
    }

    private void selectRemarkSelect() {
        List<String> stringList = mEvent.getEventRemark().getSelect().getList();
        final String[] remarkSelects = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            remarkSelects[i] = stringList.get(i);
        }
        DialogHelp.getSelectDialog(getContext(), remarkSelects, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTvRemarksSelected.setText(remarkSelects[i]);
            }
        }).show();
    }

    public EventApplyData getApplyData() {
        String name = mName.getText().toString();
        String gender = mGender.getText().toString();
        String phone = mMobile.getText().toString();
        String company = mCompany.getText().toString();
        String job = mJob.getText().toString();
        String remark = mTvRemarksSelected.getText().toString();

        if (TextUtils.isEmpty(name)) {
            AppContext.showToast("请填写姓名");
            return null;
        }

        if (TextUtils.isEmpty(phone)) {
            AppContext.showToast("请填写电话");
            return null;
        }

        if (mEvent.getEventRemark() != null && TextUtils.isEmpty(remark)) {
            AppContext.showToast("请" + mEvent.getEventRemark().getRemarkTip());
            return null;
        }

        EventApplyData data = new EventApplyData();

        data.setName(name);
        data.setGender(gender);
        data.setPhone(phone);
        data.setCompany(company);
        data.setJob(job);
        data.setRemark(remark);

        return data;
    }
}
