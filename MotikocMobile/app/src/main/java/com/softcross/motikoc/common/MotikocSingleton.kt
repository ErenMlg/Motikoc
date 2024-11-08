package com.softcross.motikoc.common

import android.util.Log
import com.softcross.motikoc.common.extensions.calculateLevel
import com.softcross.motikoc.domain.model.AIExamAnalyzeResult
import com.softcross.motikoc.domain.model.Assignment
import com.softcross.motikoc.domain.model.MotikocUser
import com.softcross.motikoc.domain.model.PlannerItem

object MotikocSingleton {

    private var currentMotikocUser: MotikocUser? = null

    fun setUser(user: MotikocUser) {
        currentMotikocUser = user
    }

    fun getAIAnalysisResult(): AIExamAnalyzeResult? {
        return currentMotikocUser?.aiExamAnalyze
    }

    fun changeAIAnalysisResult(aiExamAnalyzeResult: AIExamAnalyzeResult) {
        currentMotikocUser = currentMotikocUser?.let {
            it.copy(
                aiExamAnalyze = aiExamAnalyzeResult
            )
        }
    }

    fun changeSchedule(schedule: List<PlannerItem>) {
        currentMotikocUser = currentMotikocUser?.let {
            it.copy(
                schedule = schedule
            )
        }
    }

    fun addSchedule(schedule: PlannerItem) {
        currentMotikocUser = currentMotikocUser?.let {
            it.copy(
                schedule = it.schedule + schedule
            )
        }
    }

    fun changeMotivationMessage(message: String) {
        currentMotikocUser = currentMotikocUser?.let {
            it.copy(
                motivationMessage = message
            )
        }
    }

    fun changeAssignmentHistory(assignment: List<Assignment>) {
        currentMotikocUser = currentMotikocUser?.let {
            it.copy(
                assignmentHistory = assignment
            )
        }
    }

    fun clearUser() {
        currentMotikocUser = null
    }

    fun getUser(): MotikocUser? {
        return currentMotikocUser
    }

    fun getUserName(): String {
        return currentMotikocUser?.fullName ?: ""
    }

    fun getUserTotalXP(): Int {
        return currentMotikocUser?.totalXP ?: 0
    }

    fun getUserID(): String {
        return currentMotikocUser?.id ?: ""
    }

    fun updateUserXP(xp: Int) {
        currentMotikocUser =currentMotikocUser?.let {
            it.copy(
                totalXP =  xp,
                levelInfo = calculateLevel(xp)
            )
        }
    }

    fun changeUserInformations(
        dreamJob: String = "",
        dreamUniversity: String = "",
        dreamDepartment: String = "",
        dreamRank: String = "",
        dreamPoint: Int = 0
    ) {
        currentMotikocUser?.let {
            if (dreamJob.isNotEmpty()) it.dreamJob = dreamJob
            if (dreamUniversity.isNotEmpty()) it.dreamUniversity = dreamUniversity
            if (dreamDepartment.isNotEmpty()) it.dreamDepartment = dreamDepartment
            if (dreamRank.isNotEmpty()) it.dreamRank = dreamRank
            if (dreamPoint != 0) it.dreamPoint = dreamPoint
        }
    }

    val interestsList = listOf(
        "Teknoloji",
        "Sanat",
        "Mühendislik",
        "Sağlık",
        "Spor",
        "Bilim",
        "Sosyal Bilimler",
        "Doğa ve Çevre",
        "Hukuk ve Kamu Yönetimi",
        "Turizm ve Kültür",
        "Medya",
        "Gönüllülük ve Sosyal Sorumluluk",
        "Psikoloji",
        "Eğitim ve Pedagoji",
        "Ekonomi ve Finans",
        "Pazarlama ve Satış",
        "İnşaat ve Mimarlık",
        "Bilgisayar Bilimleri",
        "Yazılım Geliştirme",
        "Makine Mühendisliği",
        "Enerji ve Çevre Teknolojileri",
        "İletişim ve Halkla İlişkiler",
        "Dijital Pazarlama",
        "Moda ve Tasarım",
        "Gastronomi ve Yiyecek-İçecek",
        "Uluslararası İlişkiler",
        "İşletme ve Yönetim",
        "Denizcilik ve Gemi İnşaatı",
        "Havacılık ve Uzay Mühendisliği",
        "Yapay Zeka ve Makine Öğrenimi",
        "Oyun Geliştirme",
        "Robotik ve Otomasyon",
        "Biyoteknoloji",
        "Kripto Para ve Blockchain",
        "Sosyal Medya Yönetimi",
        "Siber Güvenlik",
        "Tarım ve Hayvancılık",
        "Lojistik ve Tedarik Zinciri Yönetimi",
        "Hukuk ve Adalet",
        "Fiziksel Terapi ve Rehabilitasyon",
        "Sinema ve Film Yapımı",
        "Müzik Prodüksiyonu",
        "Fotoğrafçılık",
        "Sahne Sanatları",
        "Çocuk Gelişimi",
        "Yazarlık ve Editörlük",
        "İç Mimarlık ve Dekorasyon",
        "Dijital Sanatlar ve Grafik Tasarım"
    )

    val abilityList = listOf(
        "Oyunlar",
        "Kodlama",
        "Algoritmik Problemler",
        "Yazılım Geliştirme",
        "Veritabanı Yönetimi",
        "Makine Öğrenimi",
        "Yapay Zeka",
        "Robotik",
        "Siber Güvenlik",
        "Etkili İletişim",
        "Sunum Yapma",
        "Diksiyon",
        "Topluluk Önünde Konuşma",
        "Liderlik",
        "Ekip Yönetme",
        "Proje Yönetimi",
        "Grafik Tasarım",
        "Çizim",
        "Fotoğrafçılık",
        "Müzik",
        "Video Editleme",
        "İçerik Üretme",
        "UI-UX Tasarımı",
        "Dil Öğrenme",
        "Çoklu Dil Bilme",
        "Aktif Yabancı Dil Kullanımı",
        "Çeviri ve Tercüme",
        "İstatistik",
        "Araştırma",
        "Raporlama",
        "Veri Analizi",
        "İş Zekası",
        "Pazarlama Araştırması",
        "Eğitim Verme",
        "Danışmanlık Yapma",
        "Empati",
        "Koçluk ve Mentorluk",
        "Psikolojik Danışmanlık",
        "Yatırım Yapma",
        "Kripto",
        "Borsa",
        "Finansal Analiz",
        "Portföy Yönetimi",
        "Muhasebe",
        "Finansal Planlama",
        "Dijital Pazarlama",
        "SEO-SEM Yönetimi",
        "İçerik Pazarlaması",
        "Satış Teknikleri",
        "Müşteri İlişkileri Yönetimi",
        "E-Ticaret Yönetimi",
        "Ürün Yönetimi",
        "Tedarik Zinciri Yönetimi",
        "Proje Planlama",
        "Hukuki Danışmanlık",
        "Sağlık Danışmanlığı",
        "Fiziksel Terapi",
        "Veterinerlik"
    )

    val personalProperties = listOf(
        "Sosyal",
        "Sabırlı",
        "Yaratıcı",
        "Hayal Gücü Güçlü",
        "Lider Ruhlu",
        "Sorumluluk Sahibi",
        "Vizyon Sahibi",
        "Karar Verici",
        "Mantıklı",
        "Problem Çözme Odaklı",
        "Analitik Düşünen",
        "Düzenli",
        "Disiplinli",
        "Strese Dayanıklı",
        "Soğukkanlı",
        "Esnek",
        "İşbirlikçi",
        "Güvenilir",
        "Motive Olabilen",
        "Risk Alabilen",
        "Öngörülü",
        "Uyumlu",
        "Motive Edici",
        "Pozitif Düşünen",
        "Meraklı",
        "Öğrenmeye Açık",
        "Bilgiye Aç",
        "İnisiyatif Alabilen",
        "Hırslı",
        "Detaylara Dikkat Eden",
        "Sabırlı ve Azimli",
        "Bağımsız Çalışabilen",
        "Hızlı Karar Verebilen",
        "Sorumluluk Alabilen",
        "Empatik",
        "Duygusal Zeka Yüksek",
        "Stratejik Düşünebilen",
        "Takım Oyuncusu",
        "Girişimci Ruhlu",
        "Planlı ve Organizasyonel",
        "Eleştirel Düşünebilen",
        "İkna Kabiliyeti Yüksek",
        "Adaptasyon Yeteneği Gelişmiş",
        "Zaman Yönetimi Yeteneği Gelişmiş",
        "Öz Motivasyonu Yüksek",
        "Sabırlı ve Sonuç Odaklı",
        "İletişimi Kuvvetli",
        "Yönetici Vasıflı",
        "Uzlaşmacı",
        "Hızlı Öğrenen",
        "İnovatif Düşünen"
    )

    val areaList = listOf("Sayısal", "Sözel", "Eşit Ağırlık", "Dil")
}