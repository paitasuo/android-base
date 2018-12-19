package com.czb.commlib.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Des   :
 * Author: BinarySatan
 * Time  : 2016/11/23
 */

public abstract class BaseLazyFrag extends Fragment {


    protected Context mContext;

    /**
     * 是否已经加载过数据
     */
    private boolean mHasLoadedData = false;

    private boolean mIsVisible = false;

    private boolean isCreate = false;
    /**
     * 当前Fragment渲染的视图View
     */
    private View mContentView = null;

    public Unbinder mUnbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null == mContentView) {
            isCreate = true;
            mContentView = inflater.inflate(getLayoutResID(), container, false);

            mUnbinder = ButterKnife.bind(this, mContentView);

            if (isBindEventBusHere()) {
                EventBus.getDefault().register(this);
            }

            if (null != getArguments()) {
                handleBundle(getArguments());
            }

            init(savedInstanceState);

            setUserVisibleHint(getUserVisibleHint());
        }

        return mContentView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 每次切换都会调用判断当前Fragment是否展现在屏幕上,在onAttach、onCreateView之前分别调用一次
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreate) return;
        //        LogUtil.d("BaseLazyFrag-->setUserVisibleHint()-->" + isVisibleToUser);
        //当Fragment在屏幕上可见并且没有加载过数据时调用
        if (isVisibleToUser && null != mContentView && !mHasLoadedData) {
            loadDataOnce(); // 首次可见
        } else if (isVisibleToUser) {
            onVisible();    // 非首次的每次可见
        } else {
            onInVisible();  // 不可见
        }
    }


    public void loadDataOnce() {
        mHasLoadedData = true;
        mIsVisible = true;
    }


    protected void onVisible() {
        mIsVisible = true;
    }

    protected void onInVisible() {
        mIsVisible = false;
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    @LayoutRes
    protected abstract int getLayoutResID();

    protected abstract void init(Bundle savedInstanceState);

    /**
     * 处理开启Fragment时传入的参数
     *
     * @param bundle
     */
    protected void handleBundle(Bundle bundle) {
    }


    /**
     * 是否需要注册EventBus
     *
     * @return true 则注册
     */
    protected boolean isBindEventBusHere() {
        return false;
    }


}
