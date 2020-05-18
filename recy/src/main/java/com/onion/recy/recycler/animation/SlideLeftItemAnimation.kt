
package com.onion.recy.recycler.animation

import android.animation.ObjectAnimator
import android.view.View


class SlideLeftItemAnimation : BaseItemAnimation {

    override fun onItemEnterAnimation(view: View) {
        ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0F)
            .setDuration(300)
            .start()
    }
}
