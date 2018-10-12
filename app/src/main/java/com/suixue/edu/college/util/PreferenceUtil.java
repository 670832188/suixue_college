package com.suixue.edu.college.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.dev.kit.basemodule.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.entity.InterestInfo;
import com.suixue.edu.college.entity.UserInfo;

import java.util.List;

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

    ///////////////////////////////用户模式/////////////////////////////////

    public static UserInfo getUserInfo() {
        String userInfoStr = preferences.getString(Constants.KEY_USER_INFO, "");
        if (TextUtils.isEmpty(userInfoStr)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(userInfoStr, UserInfo.class);
    }

    public static synchronized void saveUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            setStringValue(Constants.KEY_USER_INFO, null);
        } else {
            setStringValue(Constants.KEY_USER_INFO, new Gson().toJson(userInfo));
        }
    }

    /***************************************************************/

    //////////////////////以下访客模式////////////////////////
    // 获取访客出生年份
    public static String getVisitorYear() {
        return getStringValue(Constants.KEY_VISITOR_YEAR_OF_BIRTH, "");
    }

    public static void setVisitorYearOfBirth(String yearOfBirth) {
        setStringValue(Constants.KEY_VISITOR_YEAR_OF_BIRTH, yearOfBirth);
    }

    // 判断是否为游客模式：游客模式必填出生年份，并单独记录
    public static boolean isVisitorMode() {
        return !StringUtil.isEmpty(getStringValue(Constants.KEY_VISITOR_YEAR_OF_BIRTH, ""));
    }

    /**
     * 设置游客兴趣爱好
     *
     * @param interestList 游客兴趣爱好列表
     */
    public static void setVisitorInterest(List<InterestInfo> interestList) {
        if (interestList == null || interestList.size() == 0) {
            setStringValue(Constants.KEY_VISITOR_INTEREST, "");
        } else {
            setStringValue(Constants.KEY_VISITOR_INTEREST, new Gson().toJson(interestList));
        }
    }

    public static List<InterestInfo> getVisitorInterest() {
        String listJson = getStringValue(Constants.KEY_VISITOR_INTEREST, "");
        if (StringUtil.isEmpty(listJson)) {
            return null;
        } else {
            return new Gson().fromJson(listJson, new TypeToken<List<InterestInfo>>() {
            }.getType());
        }
    }

    public static void clearVisitorData() {
        preferences.edit().putString(Constants.KEY_VISITOR_YEAR_OF_BIRTH, "").apply();
        preferences.edit().putString(Constants.KEY_VISITOR_INTEREST, "").apply();
    }
}
