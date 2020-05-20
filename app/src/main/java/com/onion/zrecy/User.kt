package com.onion.zrecy

import com.onion.recy.bean.Mult

/**
 * Copyright (C), 2019-2020, 里德软件
 * FileName: User
 * Author: OnionMac by 张琦
 * Date: 2020-04-27 15:21
 * Description:
 */
class User(var name: String,var type: Int = 1): Mult {

    override fun getMultType(): Int {
        when(type){
            1 -> {
                return R.layout.item_user
            }

            2 -> {
                return R.layout.item_user2
            }
        }
        return R.layout.item_system2
    }

    val xName: String? get() {
        return "我的名字是: ${name}"
    }
}