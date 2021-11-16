package com.onion.recy.bean

import android.graphics.Color
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.onion.recy.BR
import com.onion.recy.R

/**
 * Copyright (C), 2019-2020, 里德软件
 * FileName: AdapterEmpty
 * Author: OnionMac by 张琦
 * Date: 2020/5/26 17:20
 * Description:
 */
data class AdapterEmpty(var xBg: Int = Color.WHITE,var xImg: Int = R.mipmap.list_empty,
                        var xText: String? = "None",var layout: Int = R.layout.recy_empty_page) : BaseObservable() {

    @Bindable
    var bg: Int = xBg
        set(value) {
            field = value
            notifyPropertyChanged(BR.bg)
        }

    @Bindable
    var img: Int = xImg
        set(value) {
            field = value
            notifyPropertyChanged(BR.img)
        }

    @Bindable
    var text: String? = xText
        set(value) {
            field = value
            notifyPropertyChanged(BR.text)
        }

}