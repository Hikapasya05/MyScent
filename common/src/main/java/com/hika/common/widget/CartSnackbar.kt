package com.hika.common.widget

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.hika.common.common.toRupiahFormat
import com.hika.common.databinding.CartSnackbarBinding

@SuppressLint("RestrictedApi")
class CartSnackbar (
    private val view: View,
    private val onSnackbarPressed: () -> Unit
) {

    private var binding: CartSnackbarBinding = CartSnackbarBinding.inflate(LayoutInflater.from(view.context))
    private val customSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_INDEFINITE)

    init {
        customSnackbar.view.setBackgroundColor(0) // Make the default background transparent
        binding.root.setOnClickListener { onSnackbarPressed() }
        val snackbarLayout = customSnackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.addView(binding.root, 0)
    }

    companion object {
        fun make(view: View, onSnackbarPressed: () -> Unit): CartSnackbar {
            return CartSnackbar(view, onSnackbarPressed)
        }
    }
    fun setPrice(
        price: Int,
    ) {
        binding.tvPrice.text = price.toRupiahFormat()
    }

    fun itemsCount(
        count: Int
    ) {
        binding.tvTotalItems.text = "$count items"
    }

    fun show() {
        customSnackbar.show()
    }

    fun dismiss() {
        customSnackbar.dismiss()
    }
}