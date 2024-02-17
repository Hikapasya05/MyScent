package com.hika.myscent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.hika.myscent.base.BaseDiffUtil
import com.hika.myscent.base.BaseRecyclerViewAdapter
import com.hika.myscent.databinding.ItemListReviewBinding
import com.hika.myscent.model.Review
import com.hika.myscent.widget.bind
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReviewAdapter: BaseRecyclerViewAdapter<ItemListReviewBinding, Review>() {
    override fun inflateViewBinding(parent: ViewGroup): ItemListReviewBinding {
        return ItemListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override val diffUtilBuilder: (List<Review>, List<Review>) -> DiffUtil.Callback?
        get() = { oldList, newList -> ReviewDiffUtil(oldList, newList) }

    override fun ItemListReviewBinding.binds(data: Review) {
        tvReview.text = data.review
        tvName.text = data.username

        includeRating.bind(itemView.context, defaultStars = data.rating)
        tvDate.text = data.date.toDate().toProperTimeFormat()
    }

    private fun Date.toProperTimeFormat(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        return sdf.format(this)
    }
}

class ReviewDiffUtil(
    private val oldList: List<Review>,
    private val newList: List<Review>
): BaseDiffUtil<Review, String>(oldList, newList) {
    override fun Review.getItemIdentifier(): String {
        return id
    }

}