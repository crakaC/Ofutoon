package com.crakac.ofutoon.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils

class ViewUtil private constructor() {
    companion object {
        fun getActionBarSize(context: Context): Int {
            val value = TypedValue()
            context.theme.resolveAttribute(android.R.attr.actionBarSize, value, true)
            return TypedValue.complexToDimensionPixelSize(
                value.data, context.resources.displayMetrics
            )
        }

        fun createRipple(
            palette: androidx.palette.graphics.Palette?,
            @FloatRange(from = 0.0, to = 1.0) darkAlpha: Float,
            @FloatRange(from = 0.0, to = 1.0) lightAlpha: Float,
            @ColorInt fallbackColor: Int,
            bounded: Boolean
        ): RippleDrawable {
            var rippleColor = fallbackColor
            // try the named swatches in preference order
            when {
                palette?.vibrantSwatch != null -> {
                    rippleColor = ColorUtils.setAlphaComponent(palette.vibrantSwatch!!.rgb, (darkAlpha * 255).toInt())
                }
                palette?.lightVibrantSwatch != null -> {
                    rippleColor = ColorUtils.setAlphaComponent(
                        palette.lightVibrantSwatch!!.rgb,
                        (lightAlpha * 255).toInt()
                    )
                }
                palette?.darkVibrantSwatch != null -> {
                    rippleColor = ColorUtils.setAlphaComponent(
                        palette.darkVibrantSwatch!!.rgb,
                        (darkAlpha * 255).toInt()
                    )
                }
                palette?.mutedSwatch != null -> {
                    rippleColor = ColorUtils.setAlphaComponent(palette.mutedSwatch!!.rgb, (darkAlpha * 255).toInt())
                }
                palette?.lightMutedSwatch != null -> {
                    rippleColor = ColorUtils.setAlphaComponent(
                        palette.lightMutedSwatch!!.rgb,
                        (lightAlpha * 255).toInt()
                    )
                }
                palette?.darkMutedSwatch != null -> {
                    rippleColor = ColorUtils.setAlphaComponent(palette.darkMutedSwatch!!.rgb, (darkAlpha * 255).toInt())
                }
            }
            return RippleDrawable(
                ColorStateList.valueOf(rippleColor), null,
                if (bounded) ColorDrawable(Color.WHITE) else null
            )
        }
    }
}