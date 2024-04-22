package com.hika.common.widget

import android.app.Dialog
import android.content.Context
import android.widget.LinearLayout
import com.hika.common.common.gone
import com.hika.common.databinding.DialogOrderHistoryConfirmationBinding

fun Context.buildOrderHistoryConfirmationDialog(
    title: String = "",
    message: String = "",
    showTextField: Boolean = false,
    onButtonPressed: Dialog.(String) -> Unit = {}
) = Dialog(this).apply {
    val binding = DialogOrderHistoryConfirmationBinding.inflate(layoutInflater)
    setContentView(binding.root)
    this.window?.setLayout(
        (resources.configuration.screenWidthDp * resources.displayMetrics.density * 0.9).toInt(),
        LinearLayout.LayoutParams.WRAP_CONTENT,
    )

    binding.tvTitle.text = title
    binding.tvMessage.text = message

    binding.btnConfirm.setOnClickListener {
        onButtonPressed(binding.edtReason.text.toString())
    }

    binding.tilReason.apply {
        if (showTextField) show() else gone()
    }
}