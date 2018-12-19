package com.czb.commlib.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.czb.commlib.comm.dialog.LoadingDialog;
import com.zmguanjia.commlib.R;


/**
 * Datae: 2016/8/30
 * Description:
 */
public abstract class BaseFragment<P> extends BaseLazyFrag implements BaseView<P> {
    protected View mView;
    protected P mPresenter;
    protected LoadingDialog mLoadingDialog;

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoading(String msg) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(getActivity());
            }
            mLoadingDialog.show(msg);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) mLoadingDialog.dismiss();
    }


    protected void intentJump(Class<?> clazz) {
        intentJump(clazz, null);
    }

    protected void intentJump(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
        judgeOverridePendingTransition(true);
    }

    protected void intentJumpForResult(Class<?> clazz, int requestCode) {
        intentJumpForResult(clazz, requestCode, null);
    }

    protected void intentJumpForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(mContext, clazz);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivityForResult(intent, requestCode);
    }

    protected void judgeOverridePendingTransition(boolean isIn) {
        if (isIn) {
            if (useDefOverrPendingTran())
                if (getActivity() != null)
                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            if (useDefOverrPendingTran())
                if (getActivity() != null)
                    getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }

    }

    /**
     * 将要打开activity的进入方向
     *
     * @return
     */
    protected boolean useDefOverrPendingTran() {
        return true;
    }


}
