package com.onion.zrecy

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.onion.recy.bean.AdapterEmpty
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
            .setup {
                addEmpty()
            }
        recy.baseAdapter.models = arrayListOf()



    }

}