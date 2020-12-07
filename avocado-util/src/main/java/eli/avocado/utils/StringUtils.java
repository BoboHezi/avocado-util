package eli.avocado.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具
 *
 * @author Eli Chang
 * @email eliflichang@gmail.com
 */
public class StringUtils {

    private static final String TAG = "StringUtils";

    private final static Pattern EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern IMG_URL = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)");
    private final static Pattern PHONE = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|18[0-9]|17[0-9]|14[57])[0-9]{8}$");
    private static final Pattern EMOJI = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud83e\udc00-\ud83e\udfff]",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 判断给定字符串是否空白(仅由空格、制表符、回车符、换行符组成的字符串)
     *
     * @param str
     * @return
     */
    public static boolean isAbsoluteEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是空字符串 null 或者 长度为0
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断字符串是否是合法的网址
     *
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        String regex = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        return url.matches(regex);
    }

    /**
     * 给字符串添加http
     *
     * @param url
     * @return
     */
    public static String splitHttp(String url) {
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://")
                || url.toLowerCase().startsWith("ftp://")) {
            return url;
        } else {
            return "http://" + url;
        }
    }

    /**
     * 判断字符串是否是合法的网址
     *
     * @param webIDString
     * @return
     */
    public static boolean isUrl(String webIDString) {
        if (TextUtils.isEmpty(webIDString)) {
            return false;
        }
        if (webIDString.toLowerCase().startsWith("https://") || webIDString.toLowerCase().startsWith("http://")) {
            return true;
        }
        String match = "^[0-9a-zA-Z]+[0-9a-zA-Z\\.-]*\\.[a-zA-Z]{2,4}$";
        String[] arrays = webIDString.split("/");
        for (String str : arrays) {
            if (str.matches(match)) {
                return true;
            }
        }
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(webIDString);
        while (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是合法的邮箱地址
     *
     * @param string
     * @return
     */
    public static boolean isEmailAddress(String string) {
        if (string == null || string.trim().length() == 0) {
            return false;
        }
        return EMAIL.matcher(string).matches();
    }

    /**
     * 判断字符串是否是有效的图片地址
     *
     * @param url
     * @return
     */
    public static boolean isImageUrl(String url) {
        if (url == null || url.trim().length() == 0) {
            return false;
        }
        return IMG_URL.matcher(url).matches();
    }

    /**
     * 判断字符串是否是手机号
     *
     * @param str
     * @return
     */
    public static boolean isPhoneNumber(String str) {
        if (isAbsoluteEmpty(str)) {
            return false;
        }
        return str != null && PHONE.matcher(str.trim()).matches();
    }

    /**
     * 判断字符串是否包含表情
     *
     * @param str
     * @return
     */
    public static boolean containsEmoji(String str) {
        if (isAbsoluteEmpty(str)) {
            return false;
        }
        return str != null && (EMOJI.matcher(str.trim()).matches() || containSpecialEmoji(str));
    }

    /**
     * 判断字符串是否包含表情
     *
     * @param source
     * @return
     */
    public static boolean containSpecialEmoji(CharSequence source) {
        for (int i = 0; i < source.length(); i++) {
            int code = Integer.valueOf(source.charAt(i));
            if (code >= 0x1F600 && code <= 0x1F64F
                    || code >= 0x1F300 && code <= 0x1F5FF
                    || code >= 0x1F680 && code <= 0x1F6FF
                    || code >= 0x2600 && code <= 0x26FF
                    || code >= 0x2700 && code <= 0x27BF
                    || code >= 0xFE00 && code <= 0xFE0F) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            String line;
            line = reader.readLine();
            while (line != null) {
                buffer.append(line + "<br>");
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(TAG, "toConvertString1: ", e);
        } finally {
            try {
                if (null != inputStreamReader) {
                    inputStreamReader.close();
                }
                if (null != reader) {
                    reader.close();
                    reader = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                Log.e(TAG, "toConvertString2: ", e);
            }
        }
        return buffer.toString();
    }

    /**
     * 截取字符串
     *
     * @param start
     * @param subLength
     * @param string
     * @return
     */
    public static String getSubString(int start, int subLength, String string) {
        if (string == null) {
            return "";
        }
        int length = string.length();
        start = Math.min(Math.max(0, start), length);
        subLength = subLength >= 1 ? subLength : 1;
        int end = start + subLength;
        end = end > length ? length : end;
        return string.substring(start, end);
    }

    /**
     * 拼接字符串
     *
     * @param tokens
     * @param delimiter 分隔符
     * @return
     */
    public static String join(Object[] tokens, CharSequence delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 删除所有的标点符号
     *
     * @param str
     * @return
     */
    public static String trimPunctuation(String str) {
        return str.replaceAll("[\\pP\\p{Punct}]", "");
    }

    /**
     * 将list转化为String
     *
     * @param list
     * @param delimiter 分隔符
     * @return
     */
    public static String listToStringSlipStr(List list, String delimiter) {
        StringBuilder builder = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                builder.append(list.get(i)).append(delimiter);
            }
        }
        if (builder.toString().length() > 0) {
            return builder.toString().substring(0, builder.toString().lastIndexOf(delimiter));
        } else {
            return "";
        }
    }

    /**
     * 全角字符变半角字符
     *
     * @param str
     * @return
     */
    public static String full2Half(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 65281 && c < 65373) {
                builder.append((char) (c - 65248));
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * 解析字符串返回map键值对(例：a=1&b=2 => a=1,b=2)
     *
     * @param query   源参数字符串
     * @param split1  键值对之间的分隔符（例：&）
     * @param split2  key与value之间的分隔符（例：=）
     * @param dupLink 重复参数名的参数值之间的连接符，连接后的字符串作为该参数的参数值，可为null
     *                null：不允许重复参数名出现，则靠后的参数值会覆盖掉靠前的参数值。
     * @return
     */
    public static Map<String, String> parseQuery(String query, char split1, char split2, String dupLink) {
        if (!TextUtils.isEmpty(query) && query.indexOf(split2) > 0) {
            Map<String, String> result = new HashMap<>();

            String name = null;
            String value = null;
            String tempValue;
            for (int i = 0; i < query.length(); i++) {
                char c = query.charAt(i);
                if (c == split2) {
                    value = "";
                } else if (c == split1) {
                    if (!TextUtils.isEmpty(name) && value != null) {
                        if (dupLink != null) {
                            tempValue = result.get(name);
                            if (tempValue != null) {
                                value += dupLink + tempValue;
                            }
                        }
                        result.put(name, value);
                    }
                    name = null;
                    value = null;
                } else if (value != null) {
                    value += c;
                } else {
                    name = (name != null) ? (name + c) : "" + c;
                }
            }

            if (!TextUtils.isEmpty(name) && value != null) {
                if (dupLink != null) {
                    tempValue = result.get(name);
                    if (tempValue != null) {
                        value += dupLink + tempValue;
                    }
                }
                result.put(name, value);
            }
            return result;
        }
        return null;
    }

    /**
     * 截取字符串中的数值部分
     *
     * @param params
     * @return
     */
    public static String substringNumber(String params) {
        Matcher matcher = Pattern.compile("[0-9,.%]+").matcher(params);
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            return params.substring(start, end);
        } else {
            return "0";
        }
    }
}
