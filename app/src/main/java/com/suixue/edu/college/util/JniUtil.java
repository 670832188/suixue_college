package com.suixue.edu.college.util;

/**
 * Author: cuiyan
 * Date:   18/8/23 23:04
 * Desc:
 */
public class JniUtil {
    static {
        System.loadLibrary("native-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String getPrivateKey();
}
