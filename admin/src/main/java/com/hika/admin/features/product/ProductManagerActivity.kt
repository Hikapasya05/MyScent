package com.hika.admin.features.product

import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hika.admin.databinding.ActivityProductManagerBinding
import com.hika.common.base.BaseActivity
import com.hika.common.common.gone
import com.hika.common.common.visible
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class ProductManagerActivity : BaseActivity<ActivityProductManagerBinding>() {

    protected val viewModel by viewModel<ProductManagerViewModel>()

    private val pickImageFromGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewModel.setImage(it)
        }
    }

    override fun inflateViewBinding(): ActivityProductManagerBinding {
        return ActivityProductManagerBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.PORTRAIT
    }

    override fun ActivityProductManagerBinding.bind() {

        if (fillExistingProduct()) {
            val perfumeId = intent.getStringExtra(EXTRA_PERFUME_ID) ?: return
            viewModel.getPerfume(perfumeId)
        } else {
            viewModel.pairDropDowns()
        }

        cvTakePicture.setOnClickListener {
            pickImageFromGalleryLauncher.launch("image/*")
        }

        btnSubmit.setOnClickListener {
            onButtonSubmitClicked()
        }

        lifecycleScope.launch {
            viewModel.perfume.collect {
                if (it == null) return@collect

                binding.edtName.setText(it.name)
                binding.actvCategory.setText(it.category)
                binding.edtDescription.setText(it.description)
                binding.actvStrength.setText(viewModel.toStrengthString(it.strength))
                binding.edtPrice.setText(it.price.toString())
                binding.actvAvailability.setText(viewModel.toAvailabilityString(it.isAvailable))
                binding.ivProduct.visible()
                Glide.with(this@ProductManagerActivity).load(it.image).into(ivProduct)

                viewModel.pairDropDowns()
            }
        }

        lifecycleScope.launch {
            viewModel.categories.collect {
                if (it.isEmpty()) return@collect
                binding.actvCategory.setAdapter(
                    ArrayAdapter(
                        this@ProductManagerActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                )
            }
        }

        lifecycleScope.launch {
            viewModel.strengths.collect {
                if (it.isEmpty()) return@collect
                binding.actvStrength.setAdapter(
                    ArrayAdapter(
                        this@ProductManagerActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it.map { it.second }
                    )
                )
            }
        }

        lifecycleScope.launch {
            viewModel.availabilities.collect {
                if (it.isEmpty()) return@collect
                binding.actvAvailability.setAdapter(
                    ArrayAdapter(
                        this@ProductManagerActivity,
                        android.R.layout.simple_dropdown_item_1line,
                        it
                    )
                )
            }
        }

        lifecycleScope.launch {
            viewModel.image.collect {
                if (it == null) ivProduct.gone() else ivProduct.visible()
                ivProduct.setImageURI(it);
            }
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                if (it.isLoading) loadingDialog.show() else loadingDialog.dismiss()
                if (it.isSuccess) finish()
                if (it.isError) showErrorSnackBar(it.errorMessage)
            }
        }
    }

    open fun fillExistingProduct(): Boolean {
        return false
    }

    open fun onButtonSubmitClicked() {}

    companion object {
        const val EXTRA_PERFUME_ID = "extra_perfume_id"
    }
}