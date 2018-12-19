package com.czb.commlib.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.StringRes;
import android.widget.Toast;


/**
 * Date : 2016/5/25
 * Description :
 */
public class ToastUtils {
    private static Toast mToast;
    /**
     * 系统原生类型
     **/
    private static final int SYSTYPE = 1;

    private static Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SYSTYPE:
                    if (msg.obj != null && msg.obj instanceof ToastBean) {
                        ToastBean bean = (ToastBean) msg.obj;
                        realShow(bean.text);
//                        Toast.makeText(Utils.getContext(), bean.text, Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    /**
     * 系统原生Toast
     *
     * @param text
     */
    public static void show(String text) {
        if (com.zmguanjia.commlib.util.StringUtils.isEmpty(text)) return;

        if (Looper.myLooper() != Looper.getMainLooper()) {// 非主线程
            ToastBean bean = new ToastBean();
            bean.text = text;
            Message msg = Message.obtain();
            msg.what = SYSTYPE;
            msg.obj = bean;
            mHandler.sendMessage(msg);
        } else {
            realShow(text);

        }
    }

    private static void realShow(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(Utils.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 系统原生Toast
     *
     * @param textId
     */
    public static void show(@StringRes int textId) {
        String text = Utils.getContext().getResources().getString(textId);
        show(text);
    }

    private static class ToastBean {
        private String text;
    }
}
