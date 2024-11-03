package com.softcross.motikoc.domain.repository

import com.google.android.gms.common.api.Response
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.ExamItem
import com.softcross.motikoc.domain.model.MotikocUser
import com.softcross.motikoc.domain.model.PlannerItem
import java.time.LocalDate

interface FirebaseRepository {

    suspend fun loginUser(email: String, password: String): ResponseState<MotikocUser>

    fun checkLoggedUser(): Boolean

    suspend fun registerUser(
        email: String,
        password: String,
        fullName: String
    ): ResponseState<MotikocUser>

    suspend fun addUserDetailsToFirestore(motikocUserModel: MotikocUser)

    suspend fun getUserDetailFromFirestore(): MotikocUser

    suspend fun getAssignmentsFromFirestore(userID: String): List<Assignment>

    suspend fun addJobToFirestore(jobTitle: String, userID: String)

    suspend fun addDreamsToFirestore(userID: String, dreamUniversity: String, dreamDepartment: String, dreamPoint: String, dreamRank: String)

    suspend fun addPersonalInfosToFirestore(userID: String, personalProperties: String, interests: String, abilities: String, area: String, identify: String)

    suspend fun addAssignmentToFirestore(userID: String, assignment: Assignment) : Assignment

    suspend fun updateAssignmentToFirestore(userID: String, assignment: Assignment)

    suspend fun changeUserXP(userID: String, totalXP: Int)

    suspend fun addPlanToFirestore(userID: String, plannerItem: PlannerItem)

    suspend fun getPlansFromFirestore(userID: String, date:LocalDate): List<PlannerItem>

    suspend fun addExamToFirestore(userID: String, examItem: ExamItem)

    suspend fun getExamsFromFirestore(userID: String): List<ExamItem>

    fun signOutUser()
}