package com.softcross.motikoc.domain.model

data class MotikocUser(
    val id: String = "",
    val totalXP: Int = 0,
    val levelInfo: LevelItem = LevelItem(),
    val fullName: String = "",
    var dreamJob: String = "",
    var dreamUniversity: String = "",
    var dreamDepartment: String = "",
    var dreamRank: String = "",
    var dreamPoint: Int = 0,
    var interests: String = "",
    var personalProperties: String = "",
    var abilities: String = "",
    var identify: String = "",
    var area: String = "",
    var assignmentHistory: List<Assignment> = emptyList()
)
