/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-12-19 上午10:19
 *  FileName:EmptyUtils.java
 */

package com.zhy.lib_common.utils;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.Collection;
import java.util.Map;

/**
 * Created by ZY on 2019/12/4
 * DESC: class EmptyUtils
 */
public class EmptyUtils {

    //判断EditText是否为空
    public static boolean isEmpty(EditText editText) {
        return editText == null || TextUtils.isEmpty(editText.getText().toString());
    }

    //判断集合是否为空
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    //判断map是否为空
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
