package com.hika.myscent.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<VB: ViewBinding, ListType>
    : RecyclerView.Adapter<BaseRecyclerViewAdapter<VB, ListType>.BaseViewHolder>() {

    val itemList = arrayListOf<ListType>().toMutableList()

    protected abstract fun inflateViewBinding(
        parent: ViewGroup
    ): VB

    protected abstract val diffUtilBuilder: (List<ListType>, List<ListType>) -> DiffUtil.Callback?

    var position: Int? = null
    var specificItemPosition: Int = 0
    val size get() = itemList.size
    lateinit var itemView: View

    fun submitList(data: List<ListType>) {
        //check diffUtilBuilder is null or not
        val diffUtilCallback = diffUtilBuilder(itemList, data)
        if (diffUtilCallback != null) {
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
            itemList.clear()
            itemList.addAll(data)
            diffResult.dispatchUpdatesTo(this)
        } else {
            itemList.clear()
            itemList.addAll(data)
            notifyDataSetChanged()
        }
    }

    open infix fun VB.binds(data: ListType) { }

    open fun VB.bindsWithPosition(data: ListType, position: Int) { }

    inner class BaseViewHolder(val view: VB): RecyclerView.ViewHolder(view.root) {
        fun bind(item: ListType, position: Int) {
            this@BaseRecyclerViewAdapter.itemView = itemView
            view.binds(item)
            view.bindsWithPosition(item, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = inflateViewBinding(parent)
        return BaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        this@BaseRecyclerViewAdapter.position = position
        holder.bind(itemList[position], position)
    }

    override fun getItemCount(): Int = itemList.size
    
}