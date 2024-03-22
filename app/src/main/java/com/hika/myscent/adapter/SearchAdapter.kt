package com.hika.myscent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.setMargins
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.hika.myscent.base.BaseDiffUtil
import com.hika.myscent.base.BaseRecyclerViewAdapter
import com.hika.myscent.common.toRupiahFormat
import com.hika.myscent.databinding.ItemPerfumeBinding
import com.hika.myscent.model.Perfume
import kotlin.math.roundToInt

class SearchAdapter(
    private val onItemPressed: (Perfume) -> Unit = {}
): BaseRecyclerViewAdapter<ItemPerfumeBinding, Perfume>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemPerfumeBinding {
        return ItemPerfumeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<Perfume>, List<Perfume>) -> DiffUtil.Callback = { oldList, newList ->
        SearchDiffCallback(oldList, newList)
    }

    override fun ItemPerfumeBinding.binds(data: Perfume) {
        root.layoutParams.apply {
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            val horizontalMargin = (16 * root.context.resources.displayMetrics.density).roundToInt()
            val verticalMargin = (12 * root.context.resources.displayMetrics.density).roundToInt()
            if (this is MarginLayoutParams) {
                setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
            }
        }

        tvName.text = data.name
        tvPrice.text = data.price.toRupiahFormat()
        tvRating.text = data.rating.toString()

        Glide.with(itemView.context)
            .load(data.image)
            .into(ivPerfume)

        itemView.setOnClickListener {
            onItemPressed(data)
        }
    }

    inner class SearchDiffCallback(
        private val oldList: List<Perfume>,
        private val newList: List<Perfume>
    ): BaseDiffUtil<Perfume, String>(oldList, newList) {
        override fun Perfume.getItemIdentifier(): String {
            return this.id
        }
    }
}