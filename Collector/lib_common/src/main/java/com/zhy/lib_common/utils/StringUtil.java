package com.zhy.lib_common.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

public class StringUtil {
    public static int toInteger(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int parse2Integer(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static long toLong(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 处理h5标题
     *
     * @param title
     * @return
     */
    public static String handleWebViewTitle(String title) {
        if (!TextUtils.isEmpty(title))
            return title.replace(" - 客服中心 - Oray", "");
        return "";
    }


    /**
     * 实现自动分割文本，用textview的paint逐字符测量，如果发现当前行绘制不下，就手动加入一个换行符：
     * @return
     */
    public static String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString();
        final Paint tvPaint = tv.getPaint();
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }
}
