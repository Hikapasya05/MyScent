package com.hika.admin.features.product

class AddProductActivity: ProductManagerActivity() {

    override fun onButtonSubmitClicked() {
        viewModel.postProduct(
            binding.edtName.text.toString(),
            binding.actvCategory.text.toString(),
            binding.edtDescription.text.toString(),
            binding.actvStrength.text.toString(),
            binding.edtPrice.text.toString().toInt(),
            binding.actvAvailability.text.toString()
        )
    }
}