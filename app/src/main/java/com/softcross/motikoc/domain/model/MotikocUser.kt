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
    val interests: String = "",
    val personalProperties: String = "",
    val abilities: String = "",
    val identify: String = "",
    val area: String = "",
    val assignmentHistory: List<Assignment> = emptyList(),
    val schedule: List<PlannerItem> = emptyList(),
    val motivationMessage: String = "",
    val aiExamAnalyze : AIExamAnalyzeResult? = null
)
