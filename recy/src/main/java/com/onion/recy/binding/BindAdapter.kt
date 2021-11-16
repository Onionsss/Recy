package com.onion.recy.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Copyright (C), 2019-2019, 里德软件
 * FileName: BindAdapter
 * Author: OnionMac by 张琦
 * Date: 2019/4/24 3:29 PM
 * Description: 绑定类
 */
object BindAdapter {

    @BindingAdapter("app:res")
    @JvmStatic fun res(view: ImageView, res: Int) {
        view.setImageResource(res)
    }

    @BindingAdapter("app:bg")
    @JvmStatic fun bg(view: View, res: Int) {
        view.setBackgroundResource(res)
    }

    @BindingAdapter("app:bgColor")
    @JvmStatic fun bgColor(view: View, res: Int) {
        view.setBackgroundColor(res)
    }

    @BindingAdapter("app:textColor")
    @JvmStatic fun textColor(view: TextView, res: Int) {
        view.setTextColor(view.context.resources.getColor(res))
    }
}