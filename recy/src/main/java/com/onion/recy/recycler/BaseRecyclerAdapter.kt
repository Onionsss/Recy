package com.onion.recy.recycler


import android.annotation.SuppressLint
import android.graphics.Color
import android.util.NoSuchPropertyException
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.longClicks
import com.onion.recy.BR
import com.onion.recy.R
import com.onion.recy.SizeUtils
import com.onion.recy.bean.AdapterEmpty
import com.onion.recy.bean.Empty
import com.onion.recy.bean.Mult
import com.onion.recy.databinding.RecyEmptyPageBinding
import com.onion.recy.recycler.animation.*
import java.util.concurrent.TimeUnit

/**
 * 基于DataBinding实现的通用RecyclerViewAdapter
 *
 * 一行代码创建多类型, 支持一对多类型和普通多类型混合
 * 快速添加点击事件(防暴力点击/快速点击/长按事件)
 * 点击/绑定事件回调
 * item 添加动画
 * HeaderView和FooterView的添加删除
 * 异步增量刷新数据
 * item选择状态监听(切换模式/多选/单选/全选/取消全选/反选/选中数据集/选中数量/单选和多选模式切换)
 */

@Suppress("UNCHECKED_CAST")
class BaseRecyclerAdapter(recyclerView: RecyclerView) :
    RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder>() {


    /**
     * -------------------------新代码
     */

    var emptyList: ArrayList<Any> = arrayListOf()


    init {

    }

    inline fun <reified E : Any> customEmpty(cEmpty: E, layout: Int) {
        addType<E>(layout)
        emptyList.clear()
        emptyList.add(cEmpty)
    }

    fun addEmpty(adapterEmpty: AdapterEmpty? = BaseRecyclerAdapter.sAdapterEmpty) {
        addType<AdapterEmpty> { adapterEmpty?.layout!! }
        emptyList.clear()
        emptyList.add(adapterEmpty!!)
    }

    private var mRecyclerview = recyclerView
    private var mEnableMult = true
    private var mManager = recyclerView.layoutManager

    private var enableMargin = false
    private var marginTop = 0F

    fun enableTopMargin(top: Float): BaseRecyclerAdapter {
        enableMargin = true
        marginTop = top
        return this
    }

    fun remove(pos: Int) {
        (models as ArrayList).remove(pos)
        notifyItemChanged(pos)
    }

    fun remove(model: Any) {
        (models as ArrayList).remove(model)
        notifyDataSetChanged()
    }

    fun enableMult(enable: Boolean = true): BaseRecyclerAdapter {
        mEnableMult = enable
        return this
    }

    /**
     * -------------------------新代码
     */

    var models: List<Any?>? = null
        set(value) {
            field = value

            if (models.isNullOrEmpty()) {
                if (!emptyList.isNullOrEmpty()) {
                    models = emptyList
                    mRecyclerview.layoutManager = LinearLayoutManager(mRecyclerview.context)
                }
            } else {
                mRecyclerview.layoutManager = mManager
            }

            notifyDataSetChanged()

            if (checkedPositions.isNotEmpty()) {
                checkedPositions.clear()
            }

            if (isFirst) {
                lastPosition = -1
                isFirst = false
            } else {
                lastPosition = itemCount - 1
            }
        }


    // list
    val headers: ArrayList<View> = arrayListOf()
    val footers: ArrayList<View> = arrayListOf()
    val checkedPositions = arrayListOf<Int>()
    val modelCount: Int
        get() {
            return if (models == null) {
                0
            } else {
                models!!.size
            }
        }

    val headerCount: Int
        get() {
            return headers.size
        }

    val footerCount: Int
        get() {
            return footers.size
        }


    // 单选模式
    var singleMode = false
        set(value) {
            field = value
            val size = checkedPositions.size
            if (field && size > 1) {
                for (i in 0 until size - 1) {
                    setChecked(checkedPositions[0], false)
                }
            }
        }

    private var itemAnimation: BaseItemAnimation =
        AlphaItemAnimation()
    private var lastPosition = -1
    private var isFirst = true
    private var animationEnable = false
    private var toggleMode: Boolean = false
    private val clickableIds = SparseBooleanArray()
    private val longClickableIds = ArrayList<Int>()
    private var checkableItemTypeList: List<Int>? = null
    val typePool = mutableMapOf<Class<*>, Any.(Int) -> Int>()

    // listener
    private var onBind: (BaseViewHolder.() -> Boolean)? = null
    private var onEmpty: (BaseViewHolder.() -> Unit)? = null
    private var onReload: (BaseViewHolder.() -> Unit)? = null
    private var onPayload: (BaseViewHolder.(Any) -> Unit)? = null
    private var onClick: (BaseViewHolder.(Int) -> Unit)? = null
    private var onLongClick: (BaseViewHolder.(Int) -> Unit)? = null
    private var onCheckedChange: ((Int, Int, Boolean, Boolean) -> Unit)? = null
    private var onToggle: ((Int, Int, Boolean) -> Unit)? = null
    private var onToggleEnd: ((Boolean) -> Unit)? = null

    fun onBind(block: BaseViewHolder.() -> Boolean): BaseRecyclerAdapter {
        onBind = block
        return this
    }

    fun onEmpty(block: BaseViewHolder.() -> Unit): BaseRecyclerAdapter {
        onEmpty = block
        return this
    }

    fun onReload(block: BaseViewHolder.() -> Unit): BaseRecyclerAdapter {
        onReload = block
        return this
    }

    fun onPayload(block: BaseViewHolder.(Any) -> Unit) {
        onPayload = block
    }

    fun onClick(@IdRes vararg id: Int, block: BaseViewHolder.(Int) -> Unit): BaseRecyclerAdapter {
        for (i in id) {
            clickableIds.put(i, false)
        }
        onClick = block
        return this
    }

    fun onLongClick(
        @IdRes vararg id: Int,
        block: BaseViewHolder.(Int) -> Unit
    ): BaseRecyclerAdapter {
        for (i in id) {
            longClickableIds.add(i)
        }
        onLongClick = block
        return this
    }

    /**
     * 选择变化监听器
     *
     * 条目类型
     * 条目位置
     * 是否选中
     * 是否全部选中
     * @param block BaseRecyclerAdapter.(Int, Int, Boolean, Boolean) -> Unit
     */
    fun onCheckedChange(block: (Int, Int, Boolean, Boolean) -> Unit) {
        onCheckedChange = block
    }

    fun onToggle(block: (Int, Int, Boolean) -> Unit) {
        onToggle = block
    }

    fun onToggleEnd(block: (Boolean) -> Unit) {
        onToggleEnd = block
    }

    /**
     * 返回被选中的item数量
     */
    val checkedCount: Int
        get() = checkedPositions.size

    val checkableCount: Int
        get() {
            var count = 0
            if (checkableItemTypeList == null) {
                return models!!.size
            } else {
                for (i in 0 until itemCount) {
                    if (checkableItemTypeList!!.contains(getItemViewType(i))) {
                        count++
                    }
                }
            }
            return count
        }

    fun addHeader(view: View) {
        if (headers.contains(view)) {
            return
        }
        headers.apply {
            add(view)
            notifyItemInserted(headerCount - 1)
        }
    }

    fun addFooter(view: View) {
        if (footers.contains(view)) {
            return
        }
        footers.apply {
            add(view)
            notifyItemInserted(headerCount + modelCount + footerCount - 1)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val manager = recyclerView.layoutManager

        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isFooter(position) || isHeader(position)) {
                        return manager.spanCount
                    } else {
                        return 1
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when {
            isHeader(viewType) -> BaseViewHolder(headers[viewType])
            isFooter(viewType) -> BaseViewHolder(footers[viewType - headerCount - modelCount])
            else -> {
                val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
                    LayoutInflater.from(parent.context),
                    viewType,
                    parent,
                    false
                ) ?: throw NoSuchPropertyException("item layout must is databinding layout")
                BaseViewHolder(viewDataBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (isModel(position)) {
            // 给第一个条目加marginTop
            if (enableMargin) {
                val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin =
                    if (position == 0) SizeUtils.dp2px(marginTop) else SizeUtils.dp2px(0F)
                holder.itemView.layoutParams = params
            }

            holder.bind(getModel<Any>(position)!!)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            onPayload?.invoke(holder, payloads[0])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when {
            isHeader(position) -> position
            isFooter(position) -> position
            else -> {

                val model = getModel<Any>(position)

                if (mEnableMult && model is Mult) {
                    return model.getMultType()
                }

                val modelClass: Class<*> = model!!.javaClass
                (typePool[modelClass]?.invoke(model, position)
                    ?: throw NoSuchPropertyException("please add item model type, model = $model"))
            }
        }
    }

    override fun getItemCount(): Int {
        return headerCount + modelCount + footerCount
    }


    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutPosition = holder.layoutPosition
        if (animationEnable && lastPosition < layoutPosition) {
            itemAnimation.onItemEnterAnimation(holder.itemView)
            lastPosition = layoutPosition
        }
    }

    inline fun <reified M> addType(@LayoutRes layout: Int) {
        typePool[M::class.java] = { layout }
    }

    inline fun <reified M> addType(noinline block: M.(Int) -> Int) {
        typePool[M::class.java] = block as Any.(Int) -> Int
    }

    /**
     * 添加点击事件
     * 在500毫秒内的重复点击无效
     */
    fun addClickable(@IdRes vararg id: Int) {
        for (i in id) {
            clickableIds.put(i, false)
        }
    }

    /**
     * 添加点击事件
     */
    fun addFastClickable(@IdRes vararg id: Int) {
        for (i in id) {
            clickableIds.put(i, true)
        }
    }

    /**
     * 添加长按事件
     */
    fun addLongClickable(@IdRes vararg id: Int) {
        for (i in id) {
            longClickableIds.add(i)
        }
    }

    fun setAnimation(animationEnable: Boolean) {
        this.animationEnable = animationEnable
    }

    fun setAnimation(itemAnimation: BaseItemAnimation) {
        this.animationEnable = true
        this.itemAnimation = itemAnimation
    }

    fun setAnimation(@AnimationType animationType: Int) {
        this.animationEnable = true
        when (animationType) {
            AnimationType.ALPHA -> this.itemAnimation =
                AlphaItemAnimation()
            AnimationType.SCALE -> this.itemAnimation =
                ScaleItemAnimation()
            AnimationType.SLIDE_BOTTOM -> this.itemAnimation =
                SlideBottomItemAnimation()
            AnimationType.SLIDE_LEFT -> this.itemAnimation =
                SlideLeftItemAnimation()
            AnimationType.SLIDE_RIGHT -> this.itemAnimation =
                SlideRightItemAnimation()
        }
    }

    fun removeHeader(view: View) {
        if (headers.contains(view)) {
            val temp = headers.indexOf(view)
            headers.remove(view)
            notifyItemRemoved(temp)
        }
    }

    fun removeFooter(view: View) {
        if (footers.contains(view)) {
            val temp = footers.indexOf(view)
            footers.remove(view)
            notifyItemRemoved(temp)
        }
    }

    fun clearHeader() {
        if (headers.isNotEmpty()) {
            val temp = headerCount
            headers.clear()
            notifyItemRangeRemoved(0, temp)
        }
    }

    fun clearFooter() {
        if (footers.isNotEmpty()) {
            val temp = footerCount
            footers.clear()
            notifyItemRangeRemoved(headerCount + modelCount, temp)
        }
    }

    fun isHeader(@IntRange(from = 0) position: Int): Boolean {
        return (headerCount > 0 && position < headerCount)
    }

    fun isModel(position: Int): Boolean {
        return !(isHeader(position) || isFooter(position))
    }

    fun isFooter(@IntRange(from = 0) position: Int): Boolean {
        return (footerCount > 0 && position >= headerCount + modelCount && position < headerCount + modelCount + footerCount)
    }

    fun <M> getModel(position: Int): M? {
        return when {
            isHeader(position) -> headers[position] as M
            isFooter(position) -> footers[position - headerCount - modelCount] as M
            else -> models?.let { it[position - headerCount] as M }
        }
    }

    /**
     * adapter position  convert to  model position
     * @param position Int adapter position or layout position
     * @return Int model of position
     */
    fun Int.getModelPosition(): Int {
        return this - headerCount
    }

    /**
     * 得到被选择的数据集
     */
    fun <M> getCheckedModels(): List<M> {
        val checkedModels = ArrayList<M>()
        for (position in this.checkedPositions) {
            checkedModels.add(getModel(position)!!)
        }
        return checkedModels
    }


    /**
     * 切换列表模式, 会遍历所有item
     *
     * @see .setOnItemToggleListener
     */
    fun toggle() {
        onToggle.let {
            toggleMode = !toggleMode
            for (i in 0 until itemCount) {
                onToggle?.invoke(getItemViewType(i), i, toggleMode)
            }
            onToggleEnd?.invoke(toggleMode)
        }
    }


    fun getToggleMode(): Boolean {
        return toggleMode
    }

    fun setToggleMode(toggleMode: Boolean) {
        if (this.toggleMode != toggleMode) {
            this.toggleMode = !toggleMode
            toggle()
        }
    }


    fun setCheckableType(@LayoutRes vararg checkableItemType: Int) {
        checkableItemTypeList = checkableItemType.toMutableList()
    }

    /**
     * 全选
     *
     * @see .setOnItemCheckedChangeListener
     */
    fun allChecked() {
        if (singleMode) {
            return
        }
        for (i in 0 until itemCount) {
            if (!checkedPositions.contains(i)) {
                setChecked(i, true)
            }
        }
    }

    fun allChecked(isAllChecked: Boolean) {
        if (isAllChecked) {
            allChecked()
        } else {
            clearChecked()
        }
    }

    /**
     * 取消全选
     *
     * @see .setOnItemCheckedChangeListener
     */
    fun clearChecked() {
        for (i in 0 until itemCount) {
            if (checkedPositions.contains(i)) {
                setChecked(i, false)
            }
        }
    }

    /**
     * 反选
     *
     * @see .setOnItemCheckedChangeListener
     */
    fun reverseChecked() {
        if (singleMode) {
            return
        }
        for (i in 0 until itemCount) {
            if (checkedPositions.contains(i)) {
                setChecked(i, false)
            } else {
                setChecked(i, true)
            }
        }
    }

    /**
     * 指定索引选择
     *
     * @see .setOnItemCheckedChangeListener
     */
    fun setChecked(@IntRange(from = 0) position: Int, checked: Boolean) {

        if (singleMode && checkedPositions.size == 1 && checkedPositions.contains(position)) {
            return
        }

        val itemViewType = getItemViewType(position)
        if (checkableItemTypeList != null && checkableItemTypeList!!.contains(itemViewType)) {
            return
        }
        if (onCheckedChange == null || !isModel(position)) {
            return
        }
        if (checked) {
            checkedPositions.add(position)
        } else {
            checkedPositions.remove(Integer.valueOf(position))
        }
        val isAllChecked = checkedCount == checkableCount
        onCheckedChange?.invoke(
            itemViewType,
            position,
            checked,
            isAllChecked
        )
        if (singleMode && checked && checkedPositions.size > 1) {
            setChecked(checkedPositions[0], false)
        }
    }

    fun toggleChecked(@IntRange(from = 0) position: Int) {
        if (checkedPositions.contains(position)) {
            setChecked(position, false)
        } else {
            setChecked(position, true)
        }
    }

    /**
     * 添加新数据
     */
    fun addModels(models: List<Any?>?) {
        if (models.isNullOrEmpty()) {
            return
        }
        if (this.models.isNullOrEmpty()) {
            this.models = models
        } else {
            (this.models!! as ArrayList).addAll(models)
            notifyItemRangeInserted(headerCount + modelCount, models.size)
        }
    }

    inner class BaseViewHolder : RecyclerView.ViewHolder {

        private lateinit var viewDataBinding: ViewDataBinding
        private lateinit var model: Any
        val adapter: BaseRecyclerAdapter = this@BaseRecyclerAdapter
        val modelPosition: Int
            get() {
                return adapterPosition - headerCount
            }

        constructor(itemView: View) : super(itemView)

        @SuppressLint("CheckResult")
        constructor(viewDataBinding: ViewDataBinding) : super(viewDataBinding.root) {
            this.viewDataBinding = viewDataBinding

            for (i in 0 until clickableIds.size()) {
                val view = itemView.findViewById<View>(clickableIds.keyAt(i)) ?: continue
                if (clickableIds.valueAt(i)) {
                    view.clicks().subscribe { onClick?.invoke(this, view.id) }
                } else {
                    view.clicks()
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .subscribe { onClick?.invoke(this, view.id) }
                }
            }

            for (longClickableId in longClickableIds) {
                val view = itemView.findViewById<View>(longClickableId) ?: continue
                view.longClicks().subscribe { onLongClick?.invoke(this, view.id) }
            }
        }

        fun bind(model: Any) {
            this.model = model
            when (model) {
                is AdapterEmpty -> {
                    //默认空布局
                    val binding = getViewDataBinding<RecyEmptyPageBinding>()
                    binding.recyEmptyRoot.setOnClickListener {
                        onReload?.apply { onReload!!.invoke(this@BaseViewHolder) }
                    }
                }
                is Empty -> {
                    //自定义空
                    onEmpty?.apply {
                        onEmpty!!.invoke(this@BaseViewHolder)
                    }
                }
                else -> {
                    onBind?.apply {
                        val isReturn = onBind!!.invoke(this@BaseViewHolder)
                        if (isReturn) return
                    }
                }
            }

            viewDataBinding.setVariable(dataBindingModelId, model)
            viewDataBinding.executePendingBindings()
        }

        /**
         * 该数据绑定的对象类型应该根据对应的itemType确定
         */
        fun <B : ViewDataBinding> getViewDataBinding(): B {
            return viewDataBinding as B
        }

        /**
         * 该模型的对象类型应该根据对应的itemType确定
         */
        fun <M> getModel(): M {
            return model as M
        }
    }

    companion object {
        private var dataBindingModelId: Int = -1

        /**
         * 默认的
         */
        private var sAdapterEmpty: AdapterEmpty? = AdapterEmpty()
        /**
         * 初始化
         * @param model Model id
         */
        @JvmStatic
        fun setConfig(model: Int) {
            dataBindingModelId = model
        }

        @JvmStatic
        fun setDefaultEmpty(empty: AdapterEmpty){
            sAdapterEmpty = empty
        }
    }
}