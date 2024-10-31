package com.softcross.motikoc.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.domain.model.MotikocUser
import com.softcross.motikoc.domain.repository.FirebaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : FirebaseRepository {

    override suspend fun loginUser(email: String, password: String): ResponseState<MotikocUser> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val loggedUser = getUserDetailFromFirestore()
            ResponseState.Success(loggedUser)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override fun checkLoggedUser(): Boolean = firebaseAuth.currentUser != null

    override suspend fun registerUser(
        email: String,
        password: String,
        fullName: String
    ): ResponseState<MotikocUser> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val motikocUser = MotikocUser(
                id = firebaseAuth.currentUser?.uid ?: "",
                fullName = fullName
            )
            addUserDetailsToFirestore(motikocUser)
            ResponseState.Success(motikocUser)
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun addUserDetailsToFirestore(motikocUserModel: MotikocUser) {
        val firestoreUsers = firebaseFirestore.collection("Users").document(motikocUserModel.id)
        firestoreUsers.set(
            hashMapOf(
                "fullName" to motikocUserModel.fullName
            )
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
    }

    override suspend fun getUserDetailFromFirestore(): MotikocUser {
        val userID = firebaseAuth.currentUser?.uid ?: ""
        val firestoreDoc = firebaseFirestore.collection("Users").document(userID).get().await()
        if (firestoreDoc.data != null) {
            val fullName = firestoreDoc.data?.get("fullName").toString()
            val dreamJob = (firestoreDoc.data?.get("dreamJob") ?: "").toString()
            val dreamUniversity = (firestoreDoc.data?.get("dreamUniversity") ?: "").toString()
            val dreamDepartment = (firestoreDoc.data?.get("dreamDepartment") ?: "").toString()
            val dreamRank = (firestoreDoc.data?.get("dreamRank") ?: "").toString()
            val dreamPoint = (firestoreDoc.data?.get("dreamPoint") ?: 0).toString().toInt()

            val motikocUserModel = MotikocUser(
                id = userID,
                fullName = fullName,
                dreamJob = dreamJob,
                dreamUniversity = dreamUniversity,
                dreamDepartment = dreamDepartment,
                dreamRank = dreamRank,
                dreamPoint = dreamPoint

            )

            return motikocUserModel
        } else {
            firebaseAuth.signOut()
            throw Exception("User not found")
        }
    }

    override suspend fun addJobToFirestore(jobTitle: String, userID: String) {
        val firestoreUsers = firebaseFirestore.collection("Users").document(userID)
        firestoreUsers.set(
            hashMapOf(
                "dreamJob" to jobTitle
            ),
            SetOptions.merge()
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
    }

    override suspend fun addPersonalInfosToFirestore(
        userID: String,
        personalProperties: String,
        interests: String,
        abilities: String,
        area: String,
        identify: String
    ) {
        val firestoreUsers = firebaseFirestore.collection("Users").document(userID)
        firestoreUsers.set(
            hashMapOf(
                "personalProperties" to personalProperties,
                "interests" to interests,
                "abilities" to abilities,
                "area" to area,
                "identify" to identify
            ),
            SetOptions.merge()
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
    }

    override fun signOutUser() = firebaseAuth.signOut()

}