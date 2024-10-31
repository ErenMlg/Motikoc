package com.softcross.motikoc.domain.repository

import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.MotikocUser

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

    suspend fun addJobToFirestore(jobTitle: String, userID: String)

    suspend fun addPersonalInfosToFirestore(userID: String, personalProperties: String, interests: String, abilities: String, area: String, identify: String)

    fun signOutUser()
}