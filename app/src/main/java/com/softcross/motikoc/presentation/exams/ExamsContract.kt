package com.softcross.motikoc.presentation.exams

import com.softcross.motikoc.domain.model.ExamItem
import com.softcross.motikoc.domain.model.LessonItem
import java.time.LocalDate

object ExamsContract {
    data class ExamState(
        val isLoading: Boolean = false,
        val exams: List<ExamItem> = emptyList(),
        val turkishLessonItem: LessonItem? = null,
        val historyLessonItem: LessonItem? = null,
        val geographyLessonItem: LessonItem? = null,
        val philosophyLessonItem: LessonItem? = null,
        val religionLessonItem: LessonItem? = null,
        val mathLessonItem: LessonItem? = null,
        val geometryLessonItem: LessonItem? = null,
        val physicsLessonItem: LessonItem? = null,
        val chemistryLessonItem: LessonItem? = null,
        val biologyLessonItem: LessonItem? = null,
        val examName: String = "",
        val examDate:LocalDate? = null,
        val questionCount: Int = 0,
        val questionTime: String ="",
        val isEnable:Boolean = false
    )

    sealed class ExamEvent {
        data class OnTurkishLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnHistoryLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnGeographyLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnPhilosophyLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnReligionLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnMathLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnGeometryLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnPhysicsLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnChemistryLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnBiologyLessonItemChanged(val lessonItem: LessonItem) : ExamEvent()
        data class OnExamNameChanged(val examName: String) : ExamEvent()
        data class OnExamDateChanged(val examDate: LocalDate) : ExamEvent()
        data class OnQuestionCountChanged(val questionCount: Int) : ExamEvent()
        data class OnQuestionTimeChanged(val questionTime: String) : ExamEvent()
        data object OnSaveExamClicked : ExamEvent()
    }

    sealed class ExamEffect {
        data class ShowMessage(val message: String) : ExamEffect()
    }
}