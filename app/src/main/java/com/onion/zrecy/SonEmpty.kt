package com.onion.zrecy

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.onion.recy.BR
import com.onion.recy.bean.Empty
import com.onion.recy.recycler.BaseRecyclerAdapter

/**
 * Copyright (C), 2019-2020, 里德软件
 * FileName: SonEmpty
 * Author: OnionMac by 张琦
 * Date: 2020-05-20 16:18
 * Description:
 */
data class SonEmpty(var vTips: String): BaseObservable(),Empty{

    @Bindable
    var tips: String = vTips
        set(value) {
            field = value
            notifyPropertyChanged(BR.tips)
        }

}