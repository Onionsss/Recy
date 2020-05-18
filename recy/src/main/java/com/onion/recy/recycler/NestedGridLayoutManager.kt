

package com.onion.recy.recycler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 修复被NestedScrollView嵌套的时候无法惯性滑动
 */
class NestedGridLayoutManager(
    context: Context,
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

    override fun canScrollVertically(): Boolean {
        return false
    }

}