package com.softcross.motikoc.domain.model

data class JobRecommend(
    val id: Int,
    val name: String,
    val description: String,
    val companies: String,
    val employment: String,
    val salary: String,
    val whyRecommended: String,
    val image: String
)