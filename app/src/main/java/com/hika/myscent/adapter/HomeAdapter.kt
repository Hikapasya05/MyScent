package com.hika.myscent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hika.common.base.BaseRecyclerViewAdapter
import com.hika.data.model.HomePerfume
import com.hika.myscent.databinding.ItemListHomeBinding

class HomeAdapter(
    private val onItemPressed: (String) -> Unit = {}
): BaseRecyclerViewAdapter<ItemListHomeBinding, HomePerfume>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemListHomeBinding {
        return ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<HomePerfume>, List<HomePerfume>) -> DiffUtil.Callback?
        get() = { _, _ -> null }

    override fun ItemListHomeBinding.binds(data: HomePerfume) {
        tvCategory.text = data.categoryName

        val perfumeAdapter = PerfumeAdapter(onItemPressed)
        rvCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = perfumeAdapter
        }
        perfumeAdapter.submitList(data.perfumes)
    }
}