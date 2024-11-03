package com.softcross.motikoc.domain.model

import com.google.gson.annotations.SerializedName

data class AIExamAnalyzeResult(
    @SerializedName("genel_analiz")
    val generalAnalysis: GeneralAnalysis,

    @SerializedName("ders_analizleri")
    val lessonAnalysis: List<LessonAnalysis>,

    @SerializedName("zaman_yönetimi")
    val timeManagement: TimeManagement,

    @SerializedName("gelecek_hedefler")
    val futureGoals: List<FutureGoal>
)

data class GeneralAnalysis(
    @SerializedName("güçlü_yönler")
    val strongPoints: List<String>,

    @SerializedName("gelişim_alanları")
    val improvementAreas: List<String>,

    @SerializedName("sıralama_tahmini")
    val rankingEstimation: String
)

data class LessonAnalysis(
    @SerializedName("ders")
    val lesson: String,

    @SerializedName("doğru")
    val correct: Int,

    @SerializedName("yanlış")
    val wrong: Int,

    @SerializedName("net")
    val net: Int,

    @SerializedName("öneriler")
    val suggestions: List<String>,

    @SerializedName("öncelikli_konular")
    val priorityTopics: List<String>
)

data class TimeManagement(
    @SerializedName("problem_alanları")
    val problemAreas: List<String>,

    @SerializedName("öneriler")
    val suggestions: List<String>
)

data class FutureGoal(
    @SerializedName("hedef")
    val goal: String,

    @SerializedName("süre")
    val time: String,

    @SerializedName("strateji")
    val strategy: String
)
