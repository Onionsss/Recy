package com.onion.zrecy

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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
class Mult1Activity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recy.linear(orientation = RecyclerView.VERTICAL)
            .setup {
                empty()
                addType<User>( R.layout.activity_main)
            }.onBind {
                // todo
                false
            }

        recy.baseAdapter.models = arrayListOf(
            System1("haha", 1),
            System1("haha", 2),
            System1("haha", 1),
            System1("haha", 1),
            System1("haha", 2),
            System1("hehe",123)
        )

        recy.baseAdapter.models = arrayListOf()
    }
}