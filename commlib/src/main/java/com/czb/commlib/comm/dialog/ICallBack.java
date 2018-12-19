package com.czb.commlib.comm.dialog;

/**
 * Datae: 2016/8/25
 * Description:
 */
public interface ICallBack {
    interface OneCallBack extends ICallBack {
        void clickCenter();
    }

    interface TwoCallBack extends ICallBack {
        void clickLeft();

        void clickRight();
    }
}
