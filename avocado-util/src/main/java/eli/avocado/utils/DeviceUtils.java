package eli.avocado.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.TimeZone;

/**
 * 设备工具
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
public class DeviceUtils {

    private static final String TAG = "DeviceUtils";

    /**
     * dp 转化为 px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * px 转化为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue - 0.5f) / density);
    }


    /**
     * 获取设备宽度（Dp）
     *
     * @param context
     * @return
     */
    public static int deviceWidthDP(Context context) {
        return px2dp(context, deviceWidth(context));
    }

    /**
     * 获取设备宽度（px）
     *
     * @param context
     * @return
     */
    public static int deviceWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getRealSize(outSize);
        return outSize.x;
    }

    /**
     * 获取设备高度（dp）
     *
     * @param context
     * @return
     */
    public static int deviceHeightDP(Context context) {
        return px2dp(context, deviceHeight(context));
    }

    /**
     * 获取应用所占宽度（px）
     *
     * @param context
     * @return
     */
    public static int appWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);

        return point.x;
    }

    /**
     * 获取应用所占宽度（dp）
     *
     * @param context
     * @return
     */
    public static int appWidthDP(Context context) {
        return px2dp(context, appWidth(context));
    }

    /**
     * 获取应用所占高度（px）
     *
     * @param context
     * @return
     */
    public static int appHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);

        return point.y;
    }

    public static int appHeightDP(Context context) {
        return px2dp(context, appHeight(context));
    }

    /**
     * 获取设备高度（px）
     *
     * @param context
     * @return
     */
    public static int deviceHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getRealSize(outSize);
        return outSize.y;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int statusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int navigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean hasNav = false;
        if (resourceId != 0) {
            hasNav = res.getBoolean(resourceId);
        }
        return hasNav;
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return Build.BRAND;
    }


    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 判断当前设备是不是是虚拟机
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        if (context == null) {
            return false;
        }
        try {
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            assert telephonyManager != null;
            @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
            return deviceId != null && "000000000000000".equals(deviceId) ||
                    ("sdk".equals(Build.MODEL)) || ("google_sdk".equals(Build.MODEL));
        } catch (Exception e) {
            Log.e(TAG, "isEmulator: ", e);
        }
        return false;
    }

    /**
     * 获取当前时区
     *
     * @return
     */
    public static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT);
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 0: 未知 1: 没有sim卡 2:有sim卡
     */
    public static int hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        assert telMgr != null;
        int simState = telMgr.getSimState();
        int result = 2;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = 1;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = 0;
                break;
        }
        return result;
    }

    /**
     * 返回手机运营商名称
     *
     * @return telephonyManager  1 移动，2 联通 ，3 电信，不能识别返回IMSI码
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getProvidersName(Context context) {
        String ProvidersName;
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI;
        try {
            if (telephonyManager != null) {
                IMSI = telephonyManager.getSubscriberId();
            } else {
                IMSI = "0";
            }

        } catch (Exception e) {
            IMSI = "0";
        }
        if (IMSI == null) {
            return "unknow";
        }
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "1";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "2";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "3";
        } else {
            ProvidersName = IMSI;
        }
        try {
            ProvidersName = URLEncoder.encode("" + ProvidersName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "getProvidersName: ", e);
        }
        return ProvidersName;
    }

    /**
     * 获取AndroidManifest.xml里 <meta-data>的值
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getMetaData: ", e);
        }
        return value;
    }


    /**
     * 复制到剪贴板
     *
     * @param context context
     * @param content content
     */
    public static void copy2Clipboard(Context context, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(AppUtils.getPackageName(context), content);
            clipboardManager.setPrimaryClip(clipData);
        } else {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(content);
        }
    }

    /**
     * 将px值转换为sp值
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
