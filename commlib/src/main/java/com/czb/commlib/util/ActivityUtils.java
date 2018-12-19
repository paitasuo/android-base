package com.czb.commlib.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.czb.commlib.comm.AppManager;
import com.zmguanjia.commlib.R;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Datae: 2016/8/25
 * Description:
 */
public class ActivityUtils {
    /**
     * add fragment
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }


    private static boolean mIsWaitingExit = false;

    public static void exitApp() {
        if (mIsWaitingExit) {
            mIsWaitingExit = false;
            AppManager.getAppManager().AppExit();
        } else {
            ToastUtils.show(Utils.getContext().getResources().getString(R.string.exitApp));

            mIsWaitingExit = true;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsWaitingExit = false;
                }
            }, 3000);
        }
    }

    public static void launchApp(@NonNull Context context, @NonNull String packageName,
                                 @NonNull String activityName, @Nullable Bundle b) {
        Intent intent = new Intent();
        if (b != null)
            intent.putExtras(b);

        ComponentName comp = new ComponentName(packageName, activityName);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



}
