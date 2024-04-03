package com.hika.myscent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.hika.common.base.BaseRecyclerViewAdapter
import com.hika.common.common.toRupiahFormat
import com.hika.data.model.Perfume
import com.hika.myscent.databinding.ItemListFavoriteBinding

class FavoriteAdapter(
    private val onItemPressed: (Perfume) -> Unit = {},
    private val onFavoritePressed: (String) -> Unit = {}
): BaseRecyclerViewAdapter<ItemListFavoriteBinding, Perfume>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemListFavoriteBinding {
        return ItemListFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<Perfume>, List<Perfume>) -> DiffUtil.Callback?
        get() = { _, _ -> null }

    override fun ItemListFavoriteBinding.binds(data: Perfume) {
        tvName.text = data.name
        tvPrice.text = data.price.toRupiahFormat()
        tvRating.text = data.rating.toString()

        Glide.with(itemView.context)
            .load(data.image)
            .into(ivPerfume)

        ivFavorite.setOnClickListener {
            onFavoritePressed(data.id)
        }

        itemView.setOnClickListener {
            onItemPressed(data)
        }
    }
}