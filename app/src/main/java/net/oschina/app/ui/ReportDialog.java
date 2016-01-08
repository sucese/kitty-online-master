package net.oschina.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.oschina.app.R;
import net.oschina.app.bean.Report;
import net.oschina.app.ui.dialog.CommonDialog;
import net.oschina.app.util.DialogHelp;
import net.oschina.app.util.TDevice;

public class ReportDialog extends CommonDialog implements
        android.view.View.OnClickListener {

    private TextView mTvReason;
    private TextView mTvUrl;
    private EditText mEtContent;
    private String[] reasons;
    private int reasonIndex;
    private String mUrl;
    private int mObjId;
    private byte mObjType;

    public ReportDialog(Context context, String url, int objId, byte objType) {
        this(context, R.style.dialog_common, url, objId, objType);
    }

    private ReportDialog(Context context, int defStyle, String url,
                         int objId, byte objType) {
        super(context, defStyle);
        this.mUrl = url;
        this.mObjId = objId;
        this.mObjType = objType;
        initViews(context);
    }

    private ReportDialog(Context context, boolean flag,
                         OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private void initViews(Context context) {
        reasons = getContext().getResources().getStringArray(
                R.array.report_reason);

        View view = getLayoutInflater()
                .inflate(R.layout.dialog_report, null);

        mTvReason = (TextView) view.findViewById(R.id.tv_reason);
        mTvReason.setOnClickListener(this);

        mTvReason.setText(reasons[0]);

        mTvUrl = (TextView) view.findViewById(R.id.tv_link);
        mTvUrl.setText(mUrl);

        mEtContent = (EditText) view.findViewById(R.id.et_content);

        super.setContent(view, 0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_reason) {
            selectReason();
        }
    }

    AlertDialog reson = null;

    private void selectReason() {
        reson = DialogHelp.getSingleChoiceDialog(getContext(), "举报原因", reasons, reasonIndex, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mTvReason.setText(reasons[i]);
                reasonIndex = i;
                reson.dismiss();
            }
        }).show();
    }

    public Report getReport() {
        String text = mEtContent.getText().toString();
        TDevice.hideSoftKeyboard(mEtContent);
        Report report = new Report();
        report.setObjId(mObjId);
        report.setObjType(mObjType);
        report.setUrl(mUrl);
        report.setReason(reasonIndex + 1);
        report.setOtherReason(text);
        return report;
    }
}
