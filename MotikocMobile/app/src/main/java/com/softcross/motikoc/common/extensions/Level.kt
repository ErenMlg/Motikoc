package com.softcross.motikoc.common.extensions

import com.softcross.motikoc.R
import com.softcross.motikoc.domain.model.LevelItem

fun calculateLevel(totalXP: Int): LevelItem {
    var level = 1
    var requiredXP = 500
    var increment = 100
    var previousTotalXP = 0

    while (totalXP >= previousTotalXP + requiredXP) {
        level++
        previousTotalXP += requiredXP
        requiredXP = requiredXP + (level * increment)
    }


    val progressXP = totalXP - previousTotalXP
    val levelXP = requiredXP


    val title = calculateLevelTitle(level)
    val icon = calculateLevelIcon(level)

    return LevelItem(
        level = level,
        title = title,
        requiredXP = levelXP,
        progressXP = progressXP,
        icon = icon
    )
}


fun calculateLevelTitle(level: Int): String {
    return when (level) {
        in 1..5 -> "Meraklı Civciv"
        in 6..10 -> "Azimli Karınca"
        in 11..15 -> "Kafası Zehir"
        in 16..20 -> "Başarı Avcısı"
        in 21..25 -> "Bilim Kaşifi"
        in 26..30 -> "Bilgin Tırtıl"
        else -> "Beyin Kasları"
    }
}

fun calculateLevelIcon(level: Int): Int {
    return when (level) {
        in 1..5 -> R.drawable.icon_point
        in 6..10 -> R.drawable.icon_education
        in 11..15 -> R.drawable.icon_chemical
        in 16..20 -> R.drawable.icon_departmant
        in 21..25 -> R.drawable.icon_google
        in 26..30 -> R.drawable.icon_math
        else -> R.drawable.icon_biology
    }
}