package com.czb.commlib.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/**
 * Datae: 2016/8/30
 * Description:
 */
public class PackageUtils {
    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {

        PackageInfo info = null;
        try {
            info = Utils.getContext().getPackageManager().getPackageInfo(Utils.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 获取版本号
     *
     * @return
     * @throws Exception
     */
    public static String getVersionName() {
        PackageManager packageManager = Utils.getContext().getPackageManager();
        PackageInfo packInfo = null;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(Utils.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo != null) {
            version = packInfo.versionName;
        }
        return version;
    }
}
