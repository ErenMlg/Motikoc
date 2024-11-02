package com.softcross.motikoc.domain.model

data class PlannerItem(
    val id: String = "",
    val lessonName: String,
    val topicName: String,
    val plannerDate: String,
    val workType: String,
    val isDone: Boolean = false,
    val questionCount: String = "",
    val questionCorrectCount: Int = 0,
    val questionWrongCount: Int = 0,
    val questionEmptyCount: Int = 0,
    val workTime: String = "",
    val examType: String = "",
)