
package com.onion.recy.base
import android.app.Application
import com.onion.recy.BR
import com.onion.recy.base.Recy.app
import com.onion.recy.recycler.BaseRecyclerAdapter

object Recy {
    @JvmStatic
    var app: Application? = null

    @JvmStatic
    fun init(app: Application){
        this.app = app

        BaseRecyclerAdapter.setConfig(BR.m)
    }
}


fun getApp(): Application {
    return app!!
}
