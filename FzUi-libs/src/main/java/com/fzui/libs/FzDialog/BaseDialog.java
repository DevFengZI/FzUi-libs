package com.fzui.libs.FzDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fzui.libs.R;
import com.fzui.libs.utils.WindowUtil;


public class BaseDialog extends Dialog {

    protected Context mContext;
    protected ProgressDialog mProgressDialog;

    public BaseDialog(Context context) {
        super(context, R.style.FzDialogSty);
        this.mContext = context;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        // 无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        // 背景色透明
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // 设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        } else {
            getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        }

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setCanceledOnTouchOutside(false);
        onLayoutCallBack();
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dismiss();
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK | i == KeyEvent.KEYCODE_HOME) {
                    dismiss();
                }
                return false;
            }
        });
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onLayoutCallBack();
        // 无title
        // 全屏
        setCanceledOnTouchOutside(false);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dismiss();
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK | i == KeyEvent.KEYCODE_HOME) {
                    dismiss();
                }
                return false;
            }
        });
    }

    public int[] onLayoutCallBack() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40F, mContext.getResources().getDisplayMetrics());
        Point point = WindowUtil.getScreenSize(mContext);
        int width = Math.min(point.x, point.y);
//        int height = Math.max(point.x, point.y);
        lp.width = width - space; // 宽度
//        lp.height = height / 2; // 高度
        dialogWindow.setAttributes(lp);
        return new int[]{lp.width, lp.height};
    }



    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
