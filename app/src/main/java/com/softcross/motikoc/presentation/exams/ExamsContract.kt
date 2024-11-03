package com.softcross.motikoc.presentation.exams

import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.domain.model.AIExamAnalyzeResult
import com.softcross.motikoc.domain.model.ExamItem
import com.softcross.motikoc.domain.model.LessonItem
import java.time.LocalDate

object ExamsContract {
    data class ExamState(
        val isLoading: Boolean = false,
        val isExamsLoading: Boolean = false,
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
        val examDate: LocalDate? = null,
        val questionCount: Int = 0,
        val questionTime: String = "",
        val isEnable: Boolean = false,
        val requestCount: Int = 0,
        val aiPrompt: String = """
            YKS deneme sınavı sonuçlarını analiz et ve öneriler sun.
            Lütfen aşağıdaki JSON formatında yanıt ver: dönen JSON düzgün olmalı özel karekter içermemeli.
          {
            "genel_analiz": {
                "güçlü_yönler": ["madde1", "madde2"],
                "gelişim_alanları": ["madde1", "madde2"],
                "sıralama_tahmini": "tahmin_detayı"
            },
            "ders_analizleri": [
                  {
                    "ders": "ders_adı",
                    "doğru": sayı,
                    "yanlış": sayı,
                    "net": sayı,
                    "öneriler": ["öneri1", "öneri2"],
                    "öncelikli_konular": ["konu1", "konu2"]
                  }
            ],
            "zaman_yönetimi": {
                "problem_alanları": ["alan1", "alan2"],
                "öneriler": ["öneri1", "öneri2"]
            },
            "gelecek_hedefler": [
                  {
                    "hedef": "hedef_açıklaması",
                    "süre": "hedef_süresi",
                    "strateji": "uygulama_stratejisi"
                  }
            ]
          }
          Sınav Verileri:
        """.trimIndent(),
        val aiResponse : AIExamAnalyzeResult? = MotikocSingleton.getAIAnalysisResult()
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
        data object OnLoadExams : ExamEvent()
        data object OnAIResponseClicked : ExamEvent()
    }

    sealed class ExamEffect {
        data class ShowMessage(val message: String) : ExamEffect()
    }
}