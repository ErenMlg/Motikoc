package com.softcross.motikoc.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.common.ResponseState
import com.softcross.motikoc.common.extensions.calculateLevel
import com.softcross.motikoc.common.extensions.stringToLocalDate
import com.softcross.motikoc.common.extensions.stringToLocalDateTime
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.ExamItem
import com.softcross.motikoc.domain.model.MotikocUser
import com.softcross.motikoc.domain.model.PlannerItem
import com.softcross.motikoc.domain.repository.FirebaseRepository
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
                fullName = fullName,
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
                "fullName" to motikocUserModel.fullName,
                "totalXP" to 0
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
            val personalInfo = (firestoreDoc.data?.get("personalProperties") ?: "").toString()
            val interests = (firestoreDoc.data?.get("interests") ?: "").toString()
            val abilities = (firestoreDoc.data?.get("abilities") ?: "").toString()
            val area = (firestoreDoc.data?.get("area") ?: "").toString()
            val identify = (firestoreDoc.data?.get("identify") ?: "").toString()
            val dreamPoint = (firestoreDoc.data?.get("dreamPoint") ?: 0).toString().toInt()
            val totalXP = (firestoreDoc.data?.get("totalXP") ?: 0).toString().toInt()

            val motikocUserModel = MotikocUser(
                id = userID,
                totalXP = totalXP,
                levelInfo = calculateLevel(totalXP),
                fullName = fullName,
                dreamJob = dreamJob,
                dreamUniversity = dreamUniversity,
                dreamDepartment = dreamDepartment,
                dreamRank = dreamRank,
                dreamPoint = dreamPoint,
                personalProperties = personalInfo,
                interests = interests,
                abilities = abilities,
                area = area,
                identify = identify,
                assignmentHistory = getAssignmentsFromFirestore(userID)
            )

            return motikocUserModel
        } else {
            firebaseAuth.signOut()
            throw Exception("User not found")
        }
    }

    override suspend fun getAssignmentsFromFirestore(userID: String): List<Assignment> {
        val assignmentsSnapshot =
            firebaseFirestore.collection("Assignments").whereEqualTo("userID", userID).get().await()
        if (!assignmentsSnapshot.isEmpty) {
            val assignments = mutableListOf<Assignment>()
            assignmentsSnapshot.forEach { snapshot ->
                val assignment = Assignment(
                    assignmentID = snapshot.id,
                    assignmentName = snapshot.data["assignmentName"].toString(),
                    assignmentDetail = snapshot.data["assignmentDetail"].toString(),
                    assignmentXP = snapshot.data["assignmentXP"].toString().toInt(),
                    dueDate = stringToLocalDateTime(snapshot.data["dateTime"].toString()),
                    isCompleted = snapshot.data["isAssignmentCompleted"].toString().toBoolean()
                )
                assignments.add(assignment)
            }
            return assignments
        }
        return emptyList()
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

    override suspend fun addAssignmentToFirestore(
        userID: String,
        assignment: Assignment
    ): Assignment {
        val firestoreAssignments = firebaseFirestore.collection("Assignments")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val documentReference = firestoreAssignments.add(
            hashMapOf(
                "userID" to userID,
                "assignmentName" to assignment.assignmentName,
                "assignmentDetail" to assignment.assignmentDetail,
                "assignmentXP" to assignment.assignmentXP,
                "dateTime" to assignment.dueDate.format(formatter),
                "isAssignmentCompleted" to assignment.isCompleted
            )
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
        return assignment.copy(assignmentID = documentReference.id)
    }

    override suspend fun updateAssignmentToFirestore(userID: String, assignment: Assignment) {
        try {
            val firebaseAssignment =
                firebaseFirestore.collection("Assignments").document(assignment.assignmentID)
            firebaseAssignment.set(
                hashMapOf(
                    "isAssignmentCompleted" to assignment.isCompleted
                ),
                SetOptions.merge()
            ).addOnFailureListener {
                throw Exception(it.message)
            }.await()
        } catch (e: Exception) {
            throw Exception(e.message)
        }
    }

    override suspend fun addXpToUser(userID: String, totalXP: Int) {
        val firebaseUsers =
            firebaseFirestore.collection("Users").document(userID)
        firebaseUsers.set(
            hashMapOf(
                "totalXP" to totalXP
            ),
            SetOptions.merge()
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
    }

    override suspend fun addPlanToFirestore(userID: String, plannerItem: PlannerItem) {
        val firestorePlanner = firebaseFirestore.collection("Planner")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        firestorePlanner.add(
            hashMapOf(
                "userID" to userID,
                "lessonName" to plannerItem.lessonName,
                "topicName" to plannerItem.topicName,
                "plannerDate" to plannerItem.plannerDate.format(formatter),
                "workType" to plannerItem.workType,
                "isDone" to plannerItem.isDone,
                "questionCount" to plannerItem.questionCount,
                "questionCorrectCount" to plannerItem.questionCorrectCount,
                "questionWrongCount" to plannerItem.questionWrongCount,
                "questionEmptyCount" to plannerItem.questionEmptyCount,
                "workTime" to plannerItem.workTime,
                "examType" to plannerItem.examType,
            )
        ).addOnFailureListener {
            throw Exception(it.message)
        }.await()
    }

    override suspend fun getPlansFromFirestore(
        userID: String,
        date: LocalDate
    ): ResponseState<List<PlannerItem>> {
        return try {
            val planSnapshot = firebaseFirestore.collection("Planner")
                .whereEqualTo("userID", MotikocSingleton.getUserID())
                .get()
                .await()
            if (!planSnapshot.isEmpty) {
                val plans = mutableListOf<PlannerItem>()
                planSnapshot.forEach { snapshot ->
                    plans.add(
                        PlannerItem(
                            id = snapshot.id,
                            lessonName = snapshot.data["lessonName"].toString(),
                            topicName = snapshot.data["topicName"].toString(),
                            plannerDate = snapshot.data["plannerDate"].toString(),
                            workType = snapshot.data["workType"].toString(),
                            isDone = snapshot.data["isDone"].toString().toBoolean(),
                            questionCount = snapshot.data["questionCount"].toString(),
                            questionCorrectCount = snapshot.data["questionCorrectCount"].toString()
                                .toInt(),
                            questionWrongCount = snapshot.data["questionWrongCount"].toString()
                                .toInt(),
                            questionEmptyCount = snapshot.data["questionEmptyCount"].toString()
                                .toInt(),
                            workTime = snapshot.data["workTime"].toString(),
                            examType = snapshot.data["examType"].toString()
                        )
                    )
                }
                ResponseState.Success(plans.filter { it.plannerDate.stringToLocalDate("MM-dd-yyyy") == date })
            } else {
                ResponseState.Error(Exception("No plans found"))
            }
        } catch (e: Exception) {
            ResponseState.Error(e)
        }
    }

    override suspend fun addExamToFirestore(userID: String, examItem: ExamItem) {
        val firestoreExams = firebaseFirestore.collection("Exams")
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        firestoreExams.add(
            hashMapOf(
                "userID" to userID,
                "examName" to examItem.examName,
                "examDate" to examItem.examDate.format(formatter),
                "questionCount" to examItem.questionCount,
                "examTime" to examItem.examTime,
                "turkishLesson" to examItem.turkishLessonItem,
                "historyLesson" to examItem.historyLessonItem,
                "geographyLesson" to examItem.geographyLessonItem,
                "philosophyLesson" to examItem.philosophyLessonItem,
                "religionLesson" to examItem.religionLessonItem,
                "mathLesson" to examItem.mathLessonItem,
                "geometryLesson" to examItem.geometryLessonItem,
                "physicsLesson" to examItem.physicsLessonItem,
                "chemistryLesson" to examItem.chemistryLessonItem,
                "biologyLesson" to examItem.biologyLessonItem,
                "net" to examItem.net
            )
        ).addOnSuccessListener {
            Log.e("FirebaseRepositoryImpl", "Başarılı")
        }.addOnFailureListener {
            Log.e("FirebaseRepositoryImpl", it.message.toString())
            throw Exception(it.message)
        }.await()
    }

    override fun signOutUser() = firebaseAuth.signOut()

}