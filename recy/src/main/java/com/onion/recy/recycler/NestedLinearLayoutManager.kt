

package com.onion.recy.recycler

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * 修复被NestedScrollView嵌套的时候无法惯性滑动
 */
class NestedLinearLayoutManager(
    context: Context, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun canScrollVertically(): Boolean {
        return false
    }
}