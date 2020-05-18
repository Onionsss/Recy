package com.onion.zrecy

import com.onion.recy.bean.Mult

/**
 * Copyright (C), 2019-2020, 里德软件
 * FileName: System
 * Author: OnionMac by 张琦
 * Date: 2020-04-27 15:35
 * Description:
 */
class System(var name: String,var type: Int): Mult {

    override fun getMultType(): Int {
        when(type){
            1 -> {
                return R.layout.item_system1
            }

            2 -> {
                return R.layout.item_system2
            }
        }
        return R.layout.item_system2
    }
}