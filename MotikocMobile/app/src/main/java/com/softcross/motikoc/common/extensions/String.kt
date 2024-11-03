package com.softcross.motikoc.common.extensions

import com.softcross.motikoc.R
import com.softcross.motikoc.presentation.components.lessonsHashMap

fun String.toLessonImage(): Int {
    return when (this) {
        "Matematik" -> R.drawable.icon_math
        "Geometri" -> R.drawable.icon_geo
        "Fizik" -> R.drawable.icon_physic
        "Kimya" -> R.drawable.icon_chemical
        "Biyoloji" -> R.drawable.icon_biology
        "Tarih" -> R.drawable.icon_history
        "Coğrafya" -> R.drawable.icon_geography
        "Felsefe" -> R.drawable.icon_philosopy
        "Din Kültürü" -> R.drawable.icon_mosque
        "Türkçe" -> R.drawable.icon_turkish
        "Yabancı Dil" -> R.drawable.icon_foreign_language
        else -> R.drawable.icon_math
    }
}