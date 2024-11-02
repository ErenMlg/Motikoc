package com.softcross.motikoc.domain.model

import java.time.LocalDate

data class ExamItem(
    val examID: Int = 0,
    val examName: String,
    val examDate: LocalDate,
    val examTime: String,
    val questionCount: Int,
    val turkishLessonItem: LessonItem,
    val historyLessonItem: LessonItem,
    val geographyLessonItem: LessonItem,
    val philosophyLessonItem: LessonItem,
    val religionLessonItem: LessonItem,
    val mathLessonItem: LessonItem,
    val geometryLessonItem: LessonItem,
    val physicsLessonItem: LessonItem,
    val chemistryLessonItem: LessonItem,
    val biologyLessonItem: LessonItem,
    val net: Float,
)

data class LessonItem(
    val lessonCorrectCount:Int,
    val lessonWrongCount:Int,
    val lessonEmptyCount:Int
)