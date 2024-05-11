package com.hika.common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.bumptech.glide.Glide
import com.hika.common.databinding.ImageFullSizeDialogBinding
import kotlin.math.roundToInt

fun Context.buildFullSizeImageDialog(
    image: Any?
) = Dialog(this).apply {
    val view = ImageFullSizeDialogBinding.inflate(layoutInflater)
    Glide.with(this@buildFullSizeImageDialog)
        .load(image)
        .into(view.ivFullSizeImage)
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(view.root)

    val metrics = resources.displayMetrics
    val width = metrics.widthPixels
    val height = metrics.heightPixels
    val ratio = 0.95

    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    window?.setLayout((width * ratio).roundToInt(), (height * ratio).roundToInt())
}
