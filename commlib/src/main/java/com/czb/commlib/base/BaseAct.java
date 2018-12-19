package com.czb.commlib.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.czb.commlib.comm.AppManager;
import com.czb.commlib.comm.dialog.LoadingDialog;
import com.czb.commlib.util.ActivityUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zmguanjia.commlib.R;

import butterknife.ButterKnife;

/**
 * Datae: 2016/8/29
 * Description:
 */
public abstract class BaseAct<P> extends AppCompatActivity implements BaseView<P> {

    protected Context mContext;
    /**
     * 系统状态栏颜色更改 manager
     * {@link # https://github.com/jgilfelt/SystemBarTint}
     */
    private SystemBarTintManager mTintManager;

    /**
     * 状态栏默认颜色, 可自定义颜色
     *
     * @see #setSystemBarColor(int)
     */
    protected int mDefaultSystemBarColor = Color.parseColor("#172434");


    protected P mPresenter;

    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        mContext = this;
        addToAppManager();
        setContentView(getContentLayoutId());

        ButterKnife.bind(this);
        if (isInitSystemBar()) {
            initSystemBar();
        }
        handleBundle();//参数处理
        init(savedInstanceState);
    }

    protected boolean isInitSystemBar() {
        return true;
    }


    private void addToAppManager() {
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public boolean isActive() {
        return !isFinishing();
    }

    @Override
    public void setPresenter(P persenter) {
        mPresenter = persenter;
    }

    /**
     * 初始化系统状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setNavigationBarTintEnabled(true);
            mTintManager.setTintColor(mDefaultSystemBarColor);
        }

    }

    /**
     * 设置系统状态栏颜色
     */
    public void setSystemBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= 19) {
            mTintManager.setTintColor(ContextCompat.getColor(this, color));
        }
    }

    /**
     * 如果有屏幕长亮等需要在super.onCreate()之前调用的方法, 则重新此方法
     *
     * @param savedInstanceState Bundle savedInstanceState
     */
    protected void beforeCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
//        if (getClass().getSimpleName().equals("MainAct")) {
//            ActivityUtils.exitApp();
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        judgeOverridePendingTransition(false);
    }


    @Override
    public void showLoading(String msg) {
        if (!isFinishing()) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(this);
            }
            mLoadingDialog.show(msg);
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) mLoadingDialog.dismiss();
    }

    private void handleBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            handleBundle(bundle);
        }
    }

    protected void intentJump(Class clazz) {
        intentJump(clazz, null);
    }

    protected void intentJump(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
        judgeOverridePendingTransition(true);

    }

    protected void intentJump(Intent intent){
        startActivity(intent);
        judgeOverridePendingTransition(true);
    }


    protected void judgeOverridePendingTransition(boolean isIn) {
        if (isIn) {
            if (useDefOverrPendingTran())
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            if (useDefOverrPendingTran())
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }


    }

    protected void intentJumpForResult(Class<?> clazz, int requestCode) {
        intentJumpForResult(clazz, requestCode, null);
    }

    protected void intentJumpForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        judgeOverridePendingTransition(true);

    }

    /**
     * 将要打开activity的进入方向
     *
     * @return
     */
    protected boolean useDefOverrPendingTran() {
        return true;
    }

    protected abstract int getContentLayoutId();

    /**
     * activity之间传递参数处理
     *
     * @param bundle
     */
    protected void handleBundle(Bundle bundle) {
    }

    /**
     * 初始化相关操作
     *
     * @param savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);


}
