package eli.avocado.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences 工具
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
public class PreferenceUtils {

    private static String CIRCLE_DEFAULT_NAME = "avocado";

    /**
     * 根据NAME获取shared preference
     *
     * @param context
     * @return
     */
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(CIRCLE_DEFAULT_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 根据key写入value
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, final String key, final String value) {
        if (context == null) {
            return;
        }

        final SharedPreferences settings = getPreferences(context);
        settings.edit().putString(key, value).apply();
    }

    /**
     * 根据key返回String类型值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key, final String defaultValue) {
        if (context == null) {
            return defaultValue;
        }

        return getPreferences(context).getString(key, defaultValue);
    }

    /**
     * 根据key返回String类型值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        if (context == null) {
            return null;
        }
        return getString(context, key, "");
    }

    /**
     * 设置key对应的boolean值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, final String key, final boolean value) {
        if (context == null) {
            return;
        }

        final SharedPreferences settings = getPreferences(context);
        settings.edit().putBoolean(key, value).apply();
    }

    /**
     * 根据key返回boolean类型值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, final String key, final boolean defaultValue) {
        if (context == null) {
            return defaultValue;
        }

        final SharedPreferences settings = getPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 根据key返回boolean类型值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        if (context == null) {
            return false;
        }

        return getBoolean(context, key, false);
    }

    /**
     * 设置key对应的int值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setInt(Context context, final String key, final int value) {

        if (context == null) {
            return;
        }

        final SharedPreferences settings = getPreferences(context);
        settings.edit().putInt(key, value).apply();
    }

    /**
     * 根据key返回int类型值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Context context, final String key, final int defaultValue) {

        if (context == null) {
            return defaultValue;
        }

        final SharedPreferences settings = getPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 根据key返回int类型值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, final String key) {

        if (context == null) {
            return 0;
        }

        return getInt(context, key, 0);
    }

    /**
     * 设置key对应的float值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setFloat(Context context, final String key, final float value) {

        if (context == null) {
            return;
        }

        final SharedPreferences settings = getPreferences(context);
        settings.edit().putFloat(key, value).apply();
    }

    /**
     * 根据key返回float类型值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloat(Context context, final String key, final float defaultValue) {

        if (context == null) {
            return defaultValue;
        }

        final SharedPreferences settings = getPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 根据key返回float类型值
     *
     * @param context
     * @param key
     * @return
     */
    public static float getFloat(Context context, final String key) {
        if (context == null) {
            return 0;
        }

        return getFloat(context, key, 0);
    }

    /**
     * 设置key对应的long值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setLong(Context context, final String key, final long value) {
        if (context == null) {
            return;
        }

        final SharedPreferences settings = getPreferences(context);
        settings.edit().putLong(key, value).apply();
    }

    /**
     * 根据key返回long类型值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static long getLong(Context context, final String key, final long defaultValue) {

        if (context == null) {
            return defaultValue;
        }

        final SharedPreferences settings = getPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 根据key返回long类型值
     *
     * @param context
     * @param key
     * @return
     */
    public static long getLong(Context context, final String key) {
        if (context == null) {
            return 0;
        }

        return getLong(context, key, 0);
    }

    /**
     * 清空所有的值
     *
     * @param context
     */
    public static void clearPreference(Context context) {
        if (context == null) {
            return;
        }

        final Editor editor = getPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 判断是否包含key
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean hasKey(Context context, String key) {

        return context != null && context.getSharedPreferences(CIRCLE_DEFAULT_NAME, Context.MODE_PRIVATE).contains(key);

    }
}
