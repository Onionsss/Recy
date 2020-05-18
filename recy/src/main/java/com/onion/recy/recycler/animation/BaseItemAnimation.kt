
package com.onion.recy.recycler.animation

import android.view.View


interface BaseItemAnimation {

    /**
     * 处理item被添加的时候的进入动画
     *
     * @param view item view
     */
    fun onItemEnterAnimation(view: View)
}
