package com.hika.common.widget

import android.app.Dialog
import android.content.Context
import com.hika.common.databinding.DialogCancelRejectionOrderBinding

fun Context.buildCancelRejectionOrderDialog(
    title: String,
    message: String,
    onButtonPressed: Dialog.(String) -> Unit
) = Dialog(this).apply {
    val binding = DialogCancelRejectionOrderBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.tvTitle.text = title
    binding.tvMessage.text = message

    binding.btnConfirm.setOnClickListener {
        onButtonPressed(binding.edtReason.text.toString())
    }
}