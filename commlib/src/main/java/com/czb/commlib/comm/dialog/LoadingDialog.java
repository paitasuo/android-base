package com.czb.commlib.comm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmguanjia.commlib.R;


/**
 * Datae: 2016/8/25
 * Description:
 */
public class LoadingDialog extends Dialog {

    private AnimationDrawable _ad;
    private View mContentView;
    private ImageView mIvLoading;
    private TextView mTvMessage;

    public LoadingDialog(Context context) {
        this(context, R.style.commDialogStyle_nobg);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        this.setCanceledOnTouchOutside(false);

        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.common_dialog_loading, new FrameLayout(getContext()));
        mIvLoading = (ImageView) mContentView.findViewById(R.id.iv_loading);
        mTvMessage = (TextView) mContentView.findViewById(R.id.tv_message);
        setContentView(mContentView);

    }


    public void show(String msg) {
        if (isShowing())
            return;

        if (!TextUtils.isEmpty(msg)) {
            mTvMessage.setText(msg);
            mTvMessage.setVisibility(View.VISIBLE);
        } else {
            mTvMessage.setVisibility(View.GONE);
        }

        _ad = (AnimationDrawable) mIvLoading.getDrawable();
        _ad.start();


        super.show();

    }

    public void dismiss() {
        if (this.isShowing()) {
            if (_ad != null)
                _ad.stop();

            super.dismiss();
        }
    }
}
