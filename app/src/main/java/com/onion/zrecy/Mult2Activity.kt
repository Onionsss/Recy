package com.onion.zrecy

import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
 *
 * 较复杂
 */
class Mult2Activity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recy.linear()
            .setup {  }
            .empty()

        recy.baseAdapter.models = arrayListOf(
            User(name = "zhangqi",type = 1),
            System(name = "zhangqi",type = 1),
            System(name = "zhangqi",type = 2),
            User(name = "zhangqi",type = 2),
            User(name = "zhangqi"),
            User(name = "zhangqi")
        )

        val tv = TextView(this)
        val tv2 = TextView(this)
        val tv3 = TextView(this)
        tv.text = "123"
        tv2.text = "456"
        tv3.text = "ad"
        recy.baseAdapter.addHeader(tv)
        recy.baseAdapter.addHeader(tv2)
        recy.baseAdapter.addFooter(tv3)
        recy.baseAdapter.onClick(R.id.item_root){
            val user = getModel<User>()
            Log.d("TAG","${modelPosition}")
            Log.d("TAG","${adapterPosition}")
            Log.d("TAG","${layoutPosition}")
            Log.d("TAG","${oldPosition}")
            Log.d("TAG","${user.name}")
        }
    }

}