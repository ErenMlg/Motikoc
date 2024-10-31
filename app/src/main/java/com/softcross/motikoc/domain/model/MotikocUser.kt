package com.softcross.motikoc.domain.model

data class MotikocUser(
    val id: String,
    val fullName: String,
    var dreamJob: String = "",
    var dreamUniversity: String = "",
    var dreamDepartment: String = "",
    var dreamRank: String = "",
    var dreamPoint: Int = 0,
)
