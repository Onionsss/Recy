
package com.onion.recy.base
import android.app.Application
import com.onion.recy.BR
import com.onion.recy.base.Library.app
import com.onion.recy.recycler.BaseRecyclerAdapter

object Library {
    @JvmStatic
    var app: Application? = null

    fun init(app: Application){
        this.app = app

        BaseRecyclerAdapter.setConfig(BR.m)
    }
}


fun getApp(): Application {
    return app!!
}
