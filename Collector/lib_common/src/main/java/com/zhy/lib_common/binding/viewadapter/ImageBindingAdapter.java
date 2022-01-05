/*
 *  Copyright (c) 2020 Oray Inc. All rights reserverd.
 *  No Part of this file may be reproduced, stored
 *  in a retrieval system, or transmitted, in any form, or by any means,
 *  electronic, mechanical, photocopying, recording, or otherwise,
 *  without the prior consent of Oray Inc.
 */

package com.zhy.lib_common.binding.viewadapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.databinding.BindingAdapter;


/**
 * @author ； ZY
 * @date : 2020/7/29
 * @describe : ImageView DataBindingAdapter
 */
public class ImageBindingAdapter {
    @BindingAdapter(value = {"imageUrl", "placeHolder"}, requireAll = false)
    public static void loadUrl(ImageView imageView, String url, Drawable placeHolder) {
        if (!TextUtils.isEmpty(url))
            Glide.with(imageView.getContext()).load(url).placeholder(placeHolder).into(imageView);
    }
}
