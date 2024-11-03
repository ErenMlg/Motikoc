package com.softcross.motikoc.presentation.assignments

import com.softcross.motikoc.domain.model.Assignment

object AssignmentContract {
    data class AssignmentState(
        val isLoading: Boolean = false,
        val assignments: List<Assignment> = emptyList(),
        val assignmentPrompt: String = """
            Senden türkiyede yaşayan bir genç için TYT-AYT asistanı gibi davranıp sana verdiğim görevler dışında yeni görevler vermeni istiyorum. Bu görevler sosyal, ders, konu anlatımı, test çözümü, kişisel zaman vb. olabilir. Her görev için bir XP miktarı belirlemeni istiyorum.  
            sadece 4 Adet görev vermeni istiyorum bu görevlerin 2 tanesi ders ile alakalı, 1 tanesi sosyal, 1 tanesi kişisel zaman vb. olsun

            İstediğim alanlar;
            Görev Adı: Görevi tanıtan bir başlık(Sadece 3-4Kelime)
            Görev Detayı: görevin detaylı açıklaması çok uzun olmasın
            XP: Görevin xp miktarı

            Bu verileri JSON türünde vermeni istiyorum, örnek JSON'u aşağıda paylaşıyorum;

            [
              {
                "assignmentName":"Türev'den 100 soru çöz",
                "assignmentDetail":"Türevden 100 adet soru çözüp yanlışlarını kontrol et",
                "assignmentXP":"500"
              },
              {
                "assignmentName":"Canlılardan'den 20 soru çöz",
                "assignmentDetail":"Canlılardan 20 adet soru çözüp yanlışlarını kontrol et",
                "assignmentXP":"50"
              },
              {
                "assignmentName":"Dışarıda 30 DK yürüyüş yap",
                "assignmentDetail":"Dışarıda 30 dk yürüyüş yap nefes egzersizlerini unutma",
                "assignmentXP":"300"
              }
            ]

            işte daha önce verdiğin görevler (Bu görevlerden farklı görevler vermeni istiyorum);
        """.trimIndent(),
        val totalXP: Int = 0
    )

    sealed class AssignmentAction {
        object LoadAssignments : AssignmentAction()
        data class FinishAssignment(val assignment: Assignment) : AssignmentAction()
    }

    sealed class AssignmentEffect {
        data class ShowToast(val message: String) : AssignmentEffect()
    }
}