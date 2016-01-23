package com.guoxiaoxing.kitty.ui.expansion.data;


import com.guoxiaoxing.kitty.ui.expansion.BeamBaseActivity;
import com.guoxiaoxing.kitty.presenter.base.RequiresPresenter;

/**
 * Created by Mr.Jude on 2015/8/20.
 */
@RequiresPresenter(BeamDataActivityBasePresenter.class)
public class BeamDataActivity<T extends BeamDataActivityBasePresenter,M> extends BeamBaseActivity<T> {

    public void setData(M data){}
    public void setError(Throwable e){}

}
