package com.hika.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.hika.common.base.BaseRecyclerViewAdapter
import com.hika.common.common.toRupiahFormat
import com.hika.common.databinding.ItemPerfumeBinding
import com.hika.data.model.Perfume

class PerfumeAdapter(
    private val onItemPressed: (String) -> Unit = {}
): BaseRecyclerViewAdapter<ItemPerfumeBinding, Perfume>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemPerfumeBinding {
        return ItemPerfumeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<Perfume>, List<Perfume>) -> DiffUtil.Callback?
        get() = { _, _ -> null }

    override fun ItemPerfumeBinding.binds(data: Perfume) {
        tvName.text = data.name
        tvPrice.text = data.price.toRupiahFormat()
        tvRating.text = data.rating.toString()

        Glide.with(itemView.context)
            .load(data.image)
            .into(ivPerfume)

        itemView.setOnClickListener {
            onItemPressed(data.id)
        }
    }
}