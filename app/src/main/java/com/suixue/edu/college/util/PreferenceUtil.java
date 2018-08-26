package com.suixue.edu.college.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.entity.UserInfo;

/**
 * Author: cuiyan
 * Date:   18/8/26 09:47
 * Desc:
 */
public class PreferenceUtil {

    private static SharedPreferences preferences;

    public static void init(Context context) {
        if (preferences == null) {
            synchronized (PreferenceUtil.class) {
                if (preferences == null) {
                    preferences = PreferenceManager.getDefaultSharedPreferences(context);
                }
            }
        }
    }

    public static UserInfo getUserInfo() {
        String userInfoStr = preferences.getString(Constants.USER_INFO, "");
        if (TextUtils.isEmpty(userInfoStr)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(userInfoStr, UserInfo.class);
    }

    public static synchronized void setUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            setStringValue(Constants.USER_INFO, null);
        } else {
            setStringValue(Constants.USER_INFO, new Gson().toJson(userInfo));
        }
    }

    public static synchronized void setStringValue(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public static synchronized void setIntValue(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public static synchronized void setLongValue(Context context, String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public static synchronized void setFloatValue(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public static synchronized void setBooleanValue(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }


    public static String getStringValue(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static int getIntValue(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public static long getLongValue(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public static float getFloatValue(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    public static boolean getBooleanValue(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }


}
