package com.softcross.motikoc.domain.model

import com.softcross.motikoc.R

data class LevelItem(
    val level: Int = 1,
    val title: String = "Meraklı Civciv",
    val progressXP : Int = 20,
    val requiredXP: Int = 100,
    val icon: Int = R.drawable.icon_point
)
