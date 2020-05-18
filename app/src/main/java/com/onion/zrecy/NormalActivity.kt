package com.onion.zrecy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onion.recy.recycler.baseAdapter
import com.onion.recy.recycler.linear
import com.onion.recy.recycler.setup
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Copyright (C), 2019-2020, 里德软件
 * FileName: NormalActivity
 * Author: OnionMac by 张琦
 * Date: 2020-04-27 15:28
 * Description:
 */
class NormalActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recy.linear()
            .setup {
                addType<User> { R.layout.item_user }
                empty()
            }.onBind {
                // todo
                false
            }

        recy.baseAdapter.models = arrayListOf(
            User(name = "zhangqi"),
            User(name = "zhangqi"),
            User(name = "zhangqi"),
            User(name = "zhangqi"),
            User(name = "zhangqi")
        )
    }

}