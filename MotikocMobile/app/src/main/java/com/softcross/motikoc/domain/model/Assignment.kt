package com.softcross.motikoc.domain.model

import java.time.LocalDateTime

data class Assignment(
    val assignmentID: String,
    val assignmentXP: Int,
    val assignmentName: String,
    val assignmentDetail: String,
    val dueDate: LocalDateTime,
    val isCompleted: Boolean
)
