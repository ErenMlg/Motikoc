package com.softcross.motikoc.presentation.home

import com.softcross.motikoc.common.MotikocSingleton
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.PlannerItem
import java.time.LocalDate

object HomeContract {
    data class UiState(
        val isLoading: Boolean = false,
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
        val assignments: List<Assignment> = emptyList(),
        val motivationPrompt: String = """
            Senden türkiye'de yaşayan YKS sınavına hazırlanan bir lise öğrencisi için öğrencinin özelliklerini incelemeni ve onu motivasyon cümlesi kurmanı istiyorum bu motivasyon cümlesi sınav kaygısını azaltacak, psikolojik olarak rahatlatacak ve hedefine yönelik inancı arttıracak biçimde olsun, cevap olarak sadece motivasyon cümlesi ver, motivasyon cümlesini sanki bir psikolog söylüyor gibi ver

            Analiz edebilmen için öğrencinin bilgilerini veriyorum;
            Hedef mesleğim: ${MotikocSingleton.getUser()?.dreamJob ?: "Daha belirlemedim"}
            Hedef üniversitem: ${MotikocSingleton.getUser()?.dreamUniversity ?: "Daha belirlemedim"}
            Hedef bölüm: ${MotikocSingleton.getUser()?.dreamDepartment ?: "Daha belirlemedim"}
            Hedef puan: ${MotikocSingleton.getUser()?.dreamPoint ?: "Daha belirlemedim"}
            Hedef sıralamam: ${MotikocSingleton.getUser()?.dreamRank ?: "Daha belirlemedim"}
            ilgi alanları: Çocuk Gelişimi,Sağlık,Eğitim ve Pedagoji
            kişisel özellikleri: içerik üretme,Mantıklı,Problem Çözme Odaklı
            yeteneklerim: Araştırma,yazma,okuma
            kendinden bahseden bir yazı: kendimden bahsetmek istemiyorum
        """.trimIndent(),
        val motivationMessage: String = "",
        val plannerItems: List<PlannerItem> = emptyList(),
        val plannerLoading: Boolean = false,
        val selectedDay: LocalDate = LocalDate.now(),
        val days : List<LocalDate> = emptyList(),
        val errorMessage: String = "",
        val totalUserXP : Int = MotikocSingleton.getUserTotalXP()
    )

    sealed class UiEffect {
        data object NavigateToIntroduce: UiEffect()
    }

    sealed class UiAction {
        data class FinishAssignment(val assignment: Assignment) : UiAction()
        data class FinishToDo(val toDo: PlannerItem) : UiAction()
        data class DaySelected(val day: LocalDate) : UiAction()
        data object TryAgain : UiAction()
        data object OnExitClicked : UiAction()
    }
}