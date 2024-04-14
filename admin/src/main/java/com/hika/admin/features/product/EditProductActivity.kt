package com.hika.admin.features.product

class EditProductActivity: ProductManagerActivity() {

    override fun fillExistingProduct(): Boolean {
        return true
    }

    override fun onButtonSubmitClicked() {
        viewModel.updateProduct(
            binding.edtName.text.toString(),
            binding.actvCategory.text.toString(),
            binding.edtDescription.text.toString(),
            binding.actvStrength.text.toString(),
            binding.edtPrice.text.toString().toInt(),
            binding.actvAvailability.text.toString()
        )
    }
}