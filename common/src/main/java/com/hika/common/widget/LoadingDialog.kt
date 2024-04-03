package com.hika.common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.LinearLayout
import com.hika.common.databinding.DialogLoadingBinding

fun Context.buildLoadingDialog() = Dialog(this).apply {
    val binding = DialogLoadingBinding.inflate(this.layoutInflater)
    setContentView(binding.root)
    setCanceledOnTouchOutside(false)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    this.window?.setLayout(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
}