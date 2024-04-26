package com.hika.user.features.product

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hika.common.base.BaseActivity
import com.hika.common.common.toRupiahFormat
import com.hika.user.R
import com.hika.user.adapter.ReviewAdapter
import com.hika.user.databinding.ActivityProductBinding
import com.hika.user.features.review.ReviewBottomSheetFragment
import com.hika.user.util.IntentKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductActivity : BaseActivity<ActivityProductBinding>() {

    private val viewModel by viewModel<ProductViewModel>()
    private lateinit var reviewAdapter: ReviewAdapter

    override fun inflateViewBinding(): ActivityProductBinding {
        return ActivityProductBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityProductBinding.bind() {

        val perfumeId = intent.getStringExtra(IntentKeys.PERFUME_ID).orEmpty()
        reviewAdapter = ReviewAdapter()

        rvReview.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@ProductActivity, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.isFavorite(perfumeId)
        viewModel.getPerfumeDetail(perfumeId)
        viewModel.getReviews(perfumeId)

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.productState.collect { state ->
                if (state.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (state.isSuccess) {
                    val perfume = state.successData

                    Glide.with(this@ProductActivity)
                        .load(perfume?.image)
                        .into(ivProduct)

                    tvTitle.text = perfume?.name
                    tvDescription.text = perfume?.description

                    strengthIndicator (perfume?.strength ?: 0)
                    includeProductBottomBar.tvPrice.text = (perfume?.price ?: 0).toRupiahFormat()
                }
                if (state.isError) showErrorSnackBar(state.errorMessage)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.reviews.collect { reviews ->
                reviewAdapter.submitList(reviews)
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.isFavorite.collect { isFavorite ->
                ivFavorite.setImageResource(
                    if (isFavorite) com.hika.common.R.drawable.ic_favorite
                    else R.drawable.ic_unfavorite
                )
            }
        }

        ivAddReview.setOnClickListener {
            val reviewSheet = ReviewBottomSheetFragment.newInstance(
                ::onDismiss,
                viewModel.productState.value.successData?.id.orEmpty()
            )
            reviewSheet.show(supportFragmentManager, reviewSheet.tag)
        }

        ivFavorite.setOnClickListener {
            viewModel.onFavoriteIconPressed(perfumeId)
        }

        includeProductBottomBar.btnAddCart.setOnClickListener {
            viewModel.productState.value.successData?.let {
                viewModel.addItem(
                    it.id, it.name, it.price, it.image
                )
            }
            showSuccessSnackBar("Item added to cart")
        }
    }

    private infix fun ActivityProductBinding.strengthIndicator(strength: Int) {
        includeStrongIndicator.apply {
            val indicators = listOf(
                ivIndicator1, ivIndicator2, ivIndicator3, ivIndicator4, ivIndicator5
            )

            for (i in 0 until strength) {
                indicators[i].setImageResource(R.drawable.ic_perfume_strong_active)
            }
        }

        when(strength) {
            1 -> tvStrongIndicator.text = getString(com.hika.common.R.string.indicator_weak)
            2 -> tvStrongIndicator.text = getString(com.hika.common.R.string.indicator_intimate)
            3 -> tvStrongIndicator.text = getString(com.hika.common.R.string.indicator_moderate)
            4 -> tvStrongIndicator.text = getString(com.hika.common.R.string.indicator_strong)
            5 -> tvStrongIndicator.text = getString(com.hika.common.R.string.indicator_very_strong)
        }
    }

    private fun onDismiss() {
        val perfumeId = intent.getStringExtra(IntentKeys.PERFUME_ID).orEmpty()
        viewModel.getReviews(perfumeId)
    }
}