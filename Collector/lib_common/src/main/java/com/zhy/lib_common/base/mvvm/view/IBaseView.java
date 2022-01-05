/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.base.mvvm.view;

import android.content.Context;

/**
 * Created by ZY on 2020/7/16.
 * IBaseView
 */
public interface IBaseView {
    /**
     * 初始化布局
     */
    void initView();

    /**
     * 初始化监听
     */
    void initListener();

    /**
     * 加载UI动画
     *
     * @param show 是否加载
     */
    void showInitLoadView(boolean show);

    /**
     * 获取上下文
     *
     * @return
     */
    Context getContext();
}
