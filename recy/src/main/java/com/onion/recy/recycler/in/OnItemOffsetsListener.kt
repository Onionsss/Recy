package com.onion.recy.recycler.`in`

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnItemOffsetsListener {

    fun onItemOffset(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Boolean
}
