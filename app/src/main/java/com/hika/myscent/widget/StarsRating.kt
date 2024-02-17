package com.hika.myscent.widget

import android.content.Context
import androidx.core.content.ContextCompat
import com.hika.myscent.R
import com.hika.myscent.databinding.IncludeStartsRatingBinding

fun IncludeStartsRatingBinding.bind(
    context: Context,
    defaultStars: Int = 0,
    isClickable: Boolean = false,
    starSize: Float = 52f,
    onStarPressed: (Int) -> Unit = {}
) {

    val stars = listOf(
        ivStar1,
        ivStar2,
        ivStar3,
        ivStar4,
        ivStar5
    )

    for (i in 0 until defaultStars) {
        stars[i].setImageDrawable(
            ContextCompat.getDrawable(context, R.drawable.ic_star)
                ?.constantState?.newDrawable()?.constantState?.newDrawable()
        )
    }

    stars.forEach {
        it.layoutParams.width = starSize.toInt()
        it.layoutParams.height = starSize.toInt()
    }

    if (isClickable) {
        stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                stars.forEachIndexed { i, star ->
                    if (i <= index) {
                        star.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_star)
                                ?.constantState?.newDrawable()?.constantState?.newDrawable()
                        )
                    } else {
                        star.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_unstar)
                                ?.constantState?.newDrawable()?.constantState?.newDrawable()
                        )
                    }
                }
                onStarPressed(index + 1)
            }
        }
    }

    if (!isClickable) {
        stars.forEach {
            it.isClickable = false
        }
    }
}