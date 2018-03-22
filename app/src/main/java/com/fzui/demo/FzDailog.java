package com.fzui.demo;

import android.content.Context;
import android.view.View;

import com.fzui.libs.FzDialog.BaseDialog;
import com.sdk.utils.ICubeActivity;


/**
 * @author sf
 * @name demo
 * @class name：com.fzui.libs.FzDialog
 * @class describe
 * @time 2018/3/22 上午11:28
 * @change
 * @chang time
 */
public class FzDailog extends BaseDialog implements ICubeActivity {
    private Context context;
    private View view;

    @Override
    public void initData() {

    }

    public FzDailog(final Context context) {
        super(context);
        this.context = context;
        view = View.inflate(context, R.layout.fzui_dialog_layout, null);
        setContentView(view);
        initVeiw();
        initData();
        setListener();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initVeiw() {

    }

    @Override
    public void fResetDate() {

    }

    @Override
    public void fClearDate() {

    }
}
