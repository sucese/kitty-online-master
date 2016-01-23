package com.guoxiaoxing.kitty.ui.expansion.overlay;

import android.view.View;
import android.widget.FrameLayout;

import com.guoxiaoxing.kitty.ui.expansion.BeamBaseActivity;


/**
 * Created by Mr.Jude on 2015/8/17.
 */
public abstract class ViewExpansionDelegate {
    private BeamBaseActivity activity;
    private FrameLayout container;

    public ViewExpansionDelegate(BeamBaseActivity activity) {
        this.activity = activity;
        this.container = activity.getParentView();
    }

    public final BeamBaseActivity getActivity() {
        return activity;
    }

    public final FrameLayout getContainer() {
        return container;
    }

    public void showProgressDialog(String title){}
    public void dismissProgressDialog(){}
    public View showProgressPage(){return  null;}
    public void dismissProgressPage(){}
    public View showErrorPage(){return  null;}
    public void dismissErrorPage(){}
    public void setErrorRetryListener(OnRetryListener listener){}
    public interface OnRetryListener{
        void onRetry();
    }
    public void addCustomOverlayView(View view){}
    public void removeCustomOverlayView(View view){}

}
