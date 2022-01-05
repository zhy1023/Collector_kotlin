/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  20-1-7 下午2:41
 *  FileName:TextWatcherWrapper.java
 */

package com.zhy.lib_common.wrapper;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 键盘输入监听包装类
 */
public abstract class TextWatcherWrapper implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
