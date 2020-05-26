package com.onion.zrecy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.onion.recy.recycler.baseAdapter
import com.onion.recy.recycler.grid
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
        val empty = SonEmpty("没有数据")
        recy.grid(spanCount = 1)
            .setup {
                addType<User> { R.layout.item_user }
                addType<SonEmpty> { R.layout.recy_empty_page1 }
            }.onBind {

                when(itemViewType){
                    R.layout.recy_empty_page1 -> {
                        //TODO
                    }
                }
                false
            }

        recy.baseAdapter.models = arrayListOf( SonEmpty(vTips = "没有数据"))
    }

}