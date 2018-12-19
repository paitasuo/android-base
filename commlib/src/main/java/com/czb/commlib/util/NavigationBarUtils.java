package com.czb.commlib.util;

import android.app.Activity;
import android.content.res.Resources;

/**
 * Author:BinarySatan
 * Time: 2017/8/1
 */

public class NavigationBarUtils {

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static boolean hasNavBar(Activity activity) {
        int id = activity.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return activity.getResources().getBoolean(id);
        }
        return false;
    }


}
