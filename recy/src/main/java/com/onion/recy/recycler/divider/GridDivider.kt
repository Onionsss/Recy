package com.onion.recy.recycler.divider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.onion.recy.SizeUtils


/**
 * Copyright (C), 2019-2019, 里德软件
 * FileName: GridDivider
 * Author: OnionMac by 张琦
 * Date: 2019/4/24 4:21 PM
 * Description: 间距
 */
class GridDivider(private var space: Float) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.left = SizeUtils.dp2px(space) //第一列左边贴边
            outRect.right = SizeUtils.dp2px(space)
        } else {
            if (parent.getChildAdapterPosition(view) % 2 == 1) {
                outRect.left = SizeUtils.dp2px(space)//第二列移动一个位移间距
                outRect.right = SizeUtils.dp2px(space)
            }
        }

        outRect.bottom = SizeUtils.dp2px(space)


    }
}