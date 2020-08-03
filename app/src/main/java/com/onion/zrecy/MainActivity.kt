package com.onion.zrecy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onion.recy.base.Library
import com.onion.recy.bean.AdapterEmpty
import com.onion.recy.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.activity_o.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o)

        Library.init(this.application)
        BaseRecyclerAdapter.setDefaultEmpty(AdapterEmpty(xText = "呵呵",xBg = Color.BLACK))
//        val intent = Intent(this,Mult2Activity::class.java)
//        startActivity(intent)
        normal.setOnClickListener {
            val intent = Intent(this,NormalActivity::class.java)
            startActivity(intent)
        }

        mult.setOnClickListener {
            val intent = Intent(this,Mult1Activity::class.java)
            startActivity(intent)
        }

        mult2.setOnClickListener {
            val intent = Intent(this,Mult2Activity::class.java)
            startActivity(intent)
        }
    }
}
