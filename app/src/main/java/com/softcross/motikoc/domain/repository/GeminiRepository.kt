package com.softcross.motikoc.domain.repository

import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.ChatItem
import com.softcross.motikoc.domain.model.JobRecommend
import kotlinx.coroutines.flow.Flow

interface GeminiRepository {

    fun jobRecommendChat(message: String): Flow<ResponseState<List<JobRecommend>>>

    fun chatWithAssistant(message: String): Flow<ResponseState<ChatItem>>

    fun sendAssignmentQuestion(message: String): Flow<ResponseState<List<Assignment>>>

    fun sendMotivationQuestion(message: String): Flow<ResponseState<String>>
}