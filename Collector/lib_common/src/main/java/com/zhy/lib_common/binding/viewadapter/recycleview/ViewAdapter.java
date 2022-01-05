/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.binding.viewadapter.recycleview;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Description: <ViewAdapter><br>
 * Author:      mxdl<br>
 * Date:        2019/7/4<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class ViewAdapter {
    @BindingAdapter({"linearLayoutManager"})
    public static void setLinearLayoutManager(RecyclerView recyclerView, boolean b) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(b?LinearLayoutManager.HORIZONTAL:LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
}
