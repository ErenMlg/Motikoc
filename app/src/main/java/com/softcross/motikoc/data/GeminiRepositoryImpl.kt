package com.softcross.motikoc.data

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.common.extensions.getCurrentDateTime
import com.softcross.motikoc.domain.model.AIExamAnalyzeResult
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.ChatItem
import com.softcross.motikoc.domain.model.JobRecommend
import com.softcross.motikoc.domain.repository.GeminiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GeminiRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel
) : GeminiRepository {

    override fun jobRecommendChat(message: String): Flow<ResponseState<List<JobRecommend>>> {
        return flow {
            emit(ResponseState.Loading)
            val maxRetries = 3
            var attempt = 0
            var success = false

            while (attempt < maxRetries && !success) {
                try {
                    val response = generativeModel.generateContent(message)
                    Log.e("jobRecommendChat", response.text.toString())
                    if (response.text.isNullOrBlank()) {
                        emit(ResponseState.Error(Exception("An error occurred")))
                        return@flow
                    } else {
                        val jsonResponse = response.text!!.substringAfter("```JSON")
                            .substringAfter("```json")
                            .substringAfter("{\n\"meslekler\":")
                            .substringBefore("```")
                            .trim()
                        try {
                            val gson = Gson()
                            val recommendationsResponse: List<JobRecommend> =
                                gson.fromJson(
                                    jsonResponse,
                                    object : TypeToken<List<JobRecommend>>() {}.type
                                )
                            emit(ResponseState.Success(recommendationsResponse))
                            success = true
                        } catch (e: Exception) {
                            e.printStackTrace()
                            attempt++
                        }
                    }
                } catch (e: Exception) {
                    emit(ResponseState.Error(e))
                    return@flow
                }
            }

            if (!success) {
                emit(ResponseState.Error(Exception("Failed to parse JSON after $maxRetries attempts")))
            }
        }
    }

    override fun chatWithAssistant(message: String): Flow<ResponseState<ChatItem>> {
        return flow {
            emit(ResponseState.Loading)
            try {
                val response = generativeModel.generateContent(message)
                println("Response:${response.text}\nmessage:$message")
                emit(ResponseState.Success(ChatItem(response.text.toString(), false)))
            } catch (e: Exception) {
                emit(ResponseState.Error(e))
            }
        }
    }

    override fun sendAssignmentQuestion(message: String): Flow<ResponseState<List<Assignment>>> {
        return flow {
            emit(ResponseState.Loading)
            val maxRetries = 3
            var attempt = 0
            var success = false

            while (attempt < maxRetries && !success) {
                try {
                    val response = generativeModel.generateContent(message)
                    Log.e("assignmentResponse", response.text.toString())
                    if (response.text.isNullOrBlank()) {
                        emit(ResponseState.Error(Exception("An error occurred")))
                        return@flow
                    } else {
                        val jsonResponse = response.text!!.substringAfter("```JSON")
                            .substringAfter("```json")
                            .substringBefore("```")
                            .trim()
                        try {
                            val gson = Gson()
                            val assignmentsResponse: List<Assignment> =
                                gson.fromJson<List<Assignment>?>(
                                    jsonResponse,
                                    object : TypeToken<List<Assignment>>() {}.type
                                ).map { it.copy(assignmentID = "", dueDate = getCurrentDateTime().plusDays(1)) }
                            emit(ResponseState.Success(assignmentsResponse))
                            success = true
                        } catch (e: Exception) {
                            e.printStackTrace()
                            attempt++
                        }
                    }
                } catch (e: Exception) {
                    emit(ResponseState.Error(e))
                    return@flow
                }
            }

            if (!success) {
                emit(ResponseState.Error(Exception("Failed to parse JSON after $maxRetries attempts")))
            }
        }
    }

    override fun sendMotivationQuestion(message: String): Flow<ResponseState<String>> {
        return flow {
            emit(ResponseState.Loading)
            try {
                val response = generativeModel.generateContent(message)
                println("Response:${response.text}\nmessage:$message")
                emit(ResponseState.Success(response.text.toString()))
            } catch (e: Exception) {
                emit(ResponseState.Error(e))
            }
        }
    }

    override fun sendAIAnalyzeRequest(message: String): Flow<ResponseState<AIExamAnalyzeResult>> {
        return flow {
            emit(ResponseState.Loading)
            val maxRetries = 3
            var attempt = 0
            var success = false

            while (attempt < maxRetries && !success) {
                try {

                    val response = generativeModel.generateContent(message)
                    println("Response:${response.text}")
                    if (response.text.isNullOrBlank()) {
                        emit(ResponseState.Error(Exception("An error occurred")))
                        return@flow
                    } else {
                        val jsonResponse = response.text!!.substringAfter("```JSON")
                            .substringAfter("```json")
                            .substringAfter("Response:")
                            .substringBefore("```")
                            .trim()
                        println("\nClear Response:${response.text}")
                        try {
                            val gson = Gson()
                            val assignmentsResponse: AIExamAnalyzeResult =
                                gson.fromJson<AIExamAnalyzeResult>(
                                    jsonResponse,
                                    object : TypeToken<AIExamAnalyzeResult>() {}.type
                                )
                            println(assignmentsResponse)
                            emit(ResponseState.Success(assignmentsResponse))
                            success = true
                        } catch (e: Exception) {
                            e.printStackTrace()
                            attempt++
                        }
                    }
                } catch (e: Exception) {
                    emit(ResponseState.Error(e))
                    return@flow
                }
            }

            if (!success) {
                emit(ResponseState.Error(Exception("Failed to parse JSON after $maxRetries attempts")))
            }
        }
    }

}