package com.hika.myscent.features.product

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hika.myscent.R
import com.hika.myscent.adapter.ReviewAdapter
import com.hika.myscent.base.BaseActivity
import com.hika.myscent.common.toRupiahFormat
import com.hika.myscent.databinding.ActivityProductBinding
import com.hika.myscent.features.review.ReviewBottomSheetFragment
import com.hika.myscent.util.IntentKeys
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

        viewModel.getPerfumeDetail(perfumeId)
        viewModel.getReviews(perfumeId)

        lifecycleScope.launch {
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

        lifecycleScope.launch {
            viewModel.reviews.collect { reviews ->
                reviewAdapter.submitList(reviews)
            }
        }

        ivAddReview.setOnClickListener {
            val reviewSheet = ReviewBottomSheetFragment.newInstance(
                ::onDismiss,
                viewModel.productState.value.successData?.id.orEmpty()
            )
            reviewSheet.show(supportFragmentManager, reviewSheet.tag)
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
            1 -> tvStrongIndicator.text = getString(R.string.indicator_weak)
            2 -> tvStrongIndicator.text = getString(R.string.indicator_intimate)
            3 -> tvStrongIndicator.text = getString(R.string.indicator_moderate)
            4 -> tvStrongIndicator.text = getString(R.string.indicator_strong)
            5 -> tvStrongIndicator.text = getString(R.string.indicator_very_strong)
        }
    }

    private fun onDismiss() {
        val perfumeId = intent.getStringExtra(IntentKeys.PERFUME_ID).orEmpty()
        viewModel.getReviews(perfumeId)
    }
}