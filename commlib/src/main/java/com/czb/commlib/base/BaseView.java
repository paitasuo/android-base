package com.czb.commlib.base;


/**
 * Datae: 2016/8/25
 * Description:
 */
public interface BaseView<P> {
    /**
     * 显示加载对话框,  msg可为null
     */
    void showLoading(String msg);

    void setPresenter(P presenter);


    /**
     * 隐藏加载对话框
     */
    void hideLoading();

    boolean isActive();
}
