package eli.avocado.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

/**
 * 应用工具
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
public class AppUtils {

    private static final String TAG = "AppUtils";

    /**
     * 获取应用版本号
     *
     * @param context 上下文环境
     * @return int 版本号
     */
    public static int getVersionCode(Context context) {
        int code = 1;
        if (context == null) {
            return code;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackageName(context), 0);
            if (packageInfo == null) {
                return code;
            }
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return code;
    }

    /**
     * 获取应用版本名称
     *
     * @param context 上下文环境
     * @return String 版本号名
     */
    public static String getVersionName(Context context) {
        String name = null;
        if (context == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackageName(context), 0);
            if (packageInfo == null) {
                return null;
            }
            name = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    /**
     * 获取应用程序包名
     *
     * @param context 上下文环境
     * @return String 应用程序包名
     */
    public static String getPackageName(Context context) {
        if (context == null) {
            return null;
        }
        return context.getPackageName();
    }

    /**
     * 安装指定路径应用程序安装包到手机
     *
     * @param context 上下文环境
     * @param path    应用程序安装包路径
     */
    public static void installApkFromLocalPath(Context context, String path) {
        try {
            if (StringUtils.isAbsoluteEmpty(path)) {
                return;
            }
            File file = new File(path);

            if (!file.isFile()) {
                return;
            }

            Uri apkUri;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkUri = FileProvider.getUriForFile(context, getPackageName(context) + ".fileprovider", file);
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                // 添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(new File(path));
            }
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "installApkFromLocalPath: ", e);
        }
    }

    /**
     * 获取当前App进程的id
     *
     * @return int
     */
    public static int getAppProcessId() {
        return android.os.Process.myPid();
    }

    /**
     * 获取当前App进程的Name
     *
     * @param context   context
     * @param processId processId
     * @return String
     */
    public static String getAppProcessName(Context context, int processId) {
        String processName = null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        //获取所有运行App的进程集合
        List appProcesses = manager.getRunningAppProcesses();
        Iterator iterator = appProcesses.iterator();
        while (iterator.hasNext()) {
            ActivityManager.RunningAppProcessInfo processInfo = (ActivityManager.RunningAppProcessInfo) (iterator
                    .next());
            try {
                if (processInfo.pid == processId) {
                    processName = processInfo.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }

    /**
     * 判断包是否存在
     *
     * @param packageName
     * @param context
     * @return
     */
    public static boolean isAppInstalled(String packageName, Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public static void showDefaultBrowser(Context context, String visitUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(visitUrl));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showUCBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.uc.browser", "com.uc.browser.ActivityUpdate");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    public static void showChrome1Browser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    public static void showChrome2Browser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.google.android.browser", "com.google.android.browser.BrowserActivity");
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    public static void showQQBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.tencent.mtt", "com.tencent.mtt.MainActivity");
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    public static void showOperaBrowser(Context context, String visitUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.opera.mini.android", "com.opera.mini.android.Browser");
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(visitUrl));
        context.startActivity(intent);
    }

    /**
     * 获取应用包的md5值
     *
     * @param context 上下文
     */
    public static String getMD5(Context context) {
        String signValidString = null;
        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackageName(context), PackageManager.GET_SIGNATURES);
            signValidString = getSignValidString(packageInfo.signatures[0].toByteArray());
        } catch (Exception e) {
            Log.e(TAG, "getMD5: ", e);
        }
        return getPackageName(context) + "__" + signValidString;
    }

    public static String getSignValidString(byte[] paramArrayOfByte) throws NoSuchAlgorithmException {
        MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
        localMessageDigest.update(paramArrayOfByte);
        return toHexString(localMessageDigest.digest());
    }

    public static String toHexString(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfByte.length) {
                return localStringBuilder.toString();
            }
            String str = Integer.toString(0xFF & paramArrayOfByte[i], 16);
            if (str.length() == 1) {
                str = "0" + str;
            }
            localStringBuilder.append(str);
        }
    }
}
