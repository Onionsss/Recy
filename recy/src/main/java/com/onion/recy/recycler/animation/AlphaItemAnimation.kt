
package com.onion.recy.recycler.animation

import android.animation.ObjectAnimator
import android.view.View


class AlphaItemAnimation @JvmOverloads constructor(private val mFrom: Float = DEFAULT_ALPHA_FROM) :
    BaseItemAnimation {

    override fun onItemEnterAnimation(view: View) {
        ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f).setDuration(300).start()
    }

    companion object {

        private val DEFAULT_ALPHA_FROM = 0f
    }
}
