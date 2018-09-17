package com.suixue.edu.college.config;

/**
 * Created by cuiyan on 2018/9/17.
 */
public class TextStyleConfig {
    /**
     * 字体大小
     */
    public class FontSize {
        //最大
        public static final int SIZE_18 = 18;
        public static final String KEY_SIZE_18 = "font-size:18px;";
        //默认
        public static final int SIZE_16 = 16;
        public static final String KEY_SIZE_16 = "font-size:16px;";
        //最小
        public static final int SIZE_14 = 14;
        public static final String KEY_SIZE_14 = "font-size:14px;";
    }

    /**
     * 字体对齐方式
     */
    public class FontAlign {
        //左边对齐
        public static final String KEY_ALIGN_LEFT = "text-align:left;";
        //居中对齐
        public static final String KEY_ALIGN_CENTER = "text-align:center;";
        //居右对齐
        public static final String KEY_ALIGN_RIGHT = "text-align:right;";
    }

    /**
     * 字体加粗方式
     */
    public class FontBold {
        //加粗
        public static final String KEY_STYLE_BOLD = "font-weight:bold;";
        //斜体
        public static final String KEY_STYLE_ITALIC = "font-style:italic;";
        //下划线
        public static final String KEY_STYLE_UNDERLINE = "text-decoration:underline;";
    }
}
