package com.czb.commlib.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Datae: 2016/8/30
 * Description:
 */
public class ScreenUtils {
    /**
     * * @方法说明:获取DisplayMetrics对象 * @方法名称:getDisPlayMetrics * @param context * @return
     * * @返回值:DisplayMetrics
     */
    public static DisplayMetrics getDisPlayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        if (null != context) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        }
        return metric;
    }

    /**
     * * @方法说明:获取屏幕的宽度（像素） * @方法名称:getScreenWidth * @param context * @return *
     *
     * @返回值:int
     */
    public static int getScreenWidth(Context context) {
        return getDisPlayMetrics(context).widthPixels;
    }

    /**
     * * @方法说明:获取屏幕的高（像素） * @方法名称:getScreenHeight * @param context * @return *
     *
     * @返回值:int
     */
    public static int getScreenHeight(Context context) {
        return getDisPlayMetrics(context).heightPixels;
    }

    /**
     * * @方法说明:屏幕密度(0.75 / 1.0 / 1.5) * @方法名称:getDensity * @param context * @return
     * * @返回 float
     */
    public static float getDensity(Context context) {
        return getDisPlayMetrics(context).density;
    }

    /**
     * * @方法说明:屏幕密度DPI(120 / 160 / 240) * @方法名称:getDensityDpi * @param context * @return
     * * @返回 int
     */
    public static int getDensityDpi(Context context) {
        return getDisPlayMetrics(context).densityDpi;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 设置屏幕为横屏
     * <p>还有一种就是在Activity中加属性android:screenOrientation="landscape"</p>
     * <p>不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法</p>
     *
     * @param activity activity
     */
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /**
     * 关联要监听的视图
     *
     * @param viewObserving
     */
    public void assistActivity(View viewObserving) {
        reset(viewObserving);
    }

    private  View mViewObserved;//被监听的视图
    private  int usableHeightPrevious;//视图变化前的可用高度
    private  ViewGroup.LayoutParams frameLayoutParams;

    private void reset(final View viewObserving) {
        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Log.e("onGlobalLayout", "onGlobalLayout: " );
                resetLayoutByUsableHeight(computeUsableHeight());
            }
        });


        frameLayoutParams = mViewObserved.getLayoutParams();
    }

    private void resetLayoutByUsableHeight(int usableHeightNow) {
        //比较布局变化前后的View的可用高度
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
            frameLayoutParams.height = usableHeightNow;
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**
     * 计算视图可视高度
     *
     * @return
     */
    private int computeUsableHeight() {
        Rect r = new Rect();
        mViewObserved.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
}
