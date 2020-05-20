package com.onion.zrecy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onion.recy.recycler.baseAdapter
import com.onion.recy.recycler.grid
import com.onion.recy.recycler.setup
import com.onion.zrecy.databinding.ItemUserBinding
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

        recy.grid(spanCount = 1)
            .setup {
                addType<User> { R.layout.item_user }
                empty()
            }.onBind {
                // todo
                val model = getModel<User>()  //拿到model
                val binding = getViewDataBinding<ItemUserBinding>() //拿到当前布局
                adapterPosition //当前position 如果使用了header和footer 请使用modelPosition
                modelPosition //当前position 去除了header和footer
                false
            }
        recy.baseAdapter.models = arrayListOf(
//            User(name = "zhangqi"),
//            User(name = "zhangqi"),
//            User(name = "zhangqi"),
//            User(name = "zhangqi"),
//            User(name = "zhangqi")
        )

    }

}