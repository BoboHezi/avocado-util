package eli.avocado.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * 系统工具
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
public class SystemUtils {

    private static final String TAG = "SystemUtils";

    /**
     * 获取程序版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String version = "1.0.0.0";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getVersionName: ", e);
        }
        return version;
    }

    /**
     * 获取当前应用程序的进程名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)
                return info.processName;
        }
        return "";
    }

    /**
     * 返回缺失权限集合
     *
     * @param mContexts
     * @param permissions
     * @return
     */
    public static String[] lackedPermissions(Context mContexts, String[] permissions) {
        List lackedPermissions = new ArrayList();
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                lackedPermissions.add(permission);
            }
        }
        return (String[]) lackedPermissions.toArray();
    }

    /**
     * 判断是否缺少权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param view 当前焦点view
     */
    public static void hideKeypad(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     *
     * @param view    当前焦点view
     * @param context
     */
    public static void showKeypad(View view, Context context) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 软键盘是否在活动状态
     *
     * @param view 当前焦点view
     */
    public static boolean isShowKeypad(View view) {
        if (view == null)
            return false;
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 获取视频缩略图
     *
     * @param path 视频文件
     * @return
     */
    public static Bitmap getVideoThumbnail(String path) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        Bitmap bitmap = mmr.getFrameAtTime();
        return bitmap;
    }

    /**
     * 获取正在链接的WiFi名
     *
     * @param context
     * @return
     */
    public static String getConnectWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }
}
