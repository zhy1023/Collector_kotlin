/*
 *  Copyright (c) 2019 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 *
 *  Author: ZY
 *  Create on:  19-9-18 上午9:40
 *  FileName:BaseModel.java
 */

package com.zhy.lib_common.base.mvp;

/**
 * Created by ZY on 2019/9/18
 * DESC: class BaseModel
 */
public abstract class BaseModel<P extends BasePresenter, CONTRACT> {
    protected P p;

    public BaseModel(P p) {
        this.p = p;
    }

    public abstract CONTRACT getContract();
}
