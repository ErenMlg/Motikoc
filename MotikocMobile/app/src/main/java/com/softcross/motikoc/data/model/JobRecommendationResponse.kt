package com.softcross.motikoc.data.model

import com.softcross.motikoc.domain.model.JobRecommend

data class JobRecommendationResponse(
    val recommendations: List<JobRecommend>
)
