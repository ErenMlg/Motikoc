package com.softcross.motikoc.presentation.jobSelection

import com.softcross.motikoc.domain.model.JobRecommend

object JobSelectionContract {
    data class UiState(
        val isLoading: Boolean = false,
        val jobRecommendList: List<JobRecommend> = emptyList(),
        val retryCount: Int = 0,
        val prompt: String = """
            Merhaba,
            Senden bir meslek öneri sistemi gibi davranmanı istiyorum. Sana Türkiyedeki bir lise öğrencisi için ilgi alanları, kişisel özellikleri, Yetenek ve becerileri, alan tercihleri(sayısal,sözel, vb.) ve kendini tanıtan bir yazı vereceğim bu bilgileri analiz ederek kullanıcıya 5 adet meslek önerisi yapmanı istiyorum.

            Meslek önerirken mesleğin adı, mesleğin açıklaması(ne iş yaptığı hakkında ufak bir bilgi yazısı), bu meslek ile ilgili türkiyedeki sektördeki bilinen şirketlerden örnekler, iş bulma zorluğu (orta,düşük,yüksek şeklinde), ortalama maaş(orta,yüksek,tatmin edici şeklinde), bu mesleği neden önerdiğin, bu mesleğin benim ile uyuşma oranı(%86) bilgilerinin olmasını istiyorum.

            Cevabı JSON olarak ver.
            İstediğim JSON Dönüşü şu şekilde(image yerine şu urlleri rastgele sırayla yerleştir, her resimi sadece bir defa kullan(
            https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D
            https://images.unsplash.com/photo-1653566031535-bcf33e1c2893?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D
            https://plus.unsplash.com/premium_photo-1661598213264-9b708f59d793?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D
            https://images.unsplash.com/photo-1499750310107-5fef28a66643?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D
            https://plus.unsplash.com/premium_photo-1661284828052-ea25d6ea94cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D
            );

                        [
                        {
                        "name": "Kullanıcı Deneyimi (UX) Tasarımcısı",
                        "description": "UX Tasarımcıları, ürün ve hizmetlerin kullanıcılar için mümkün olduğunca kullanışlı, erişilebilir ve keyifli olmasını sağlar. Araştırma, prototipleme, tasarım ve test gibi süreçleri kullanarak kullanıcı deneyimini optimize ederler.",
                        "companies": "Google, Microsoft, Amazon, Apple, Facebook, Spotify, Airbnb",
                        "employment": "Orta",
                        "salary": "Orta",
                        "whyRecommended": "Teknoloji ve tasarım merakın, hayal gücün ve iletişim becerilerin bu mesleğe uygun olduğunu gösteriyor.  Grafik tasarım, fotoğrafçılık ve araştırma yeteneklerin de UX tasarımcısı olmak için önemli avantajlar sağlayacaktır.",
                        "image": "https://plus.unsplash.com/premium_photo-1661284828052-ea25d6ea94cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "percentage":"%86"
                        },
                        {
                        "name": "E-Spor Oyuncusu",
                        "description": "E-Spor Oyuncuları, belirli video oyunlarında profesyonel olarak yarışırlar.  Takım çalışması, stratejik düşünme, hızlı refleksler ve sürekli pratik yapma becerileri gerektirir. Turnuvalara katılarak ödüller kazanırlar.",
                        "companies": "Riot Games, Fnatic, Team Liquid, G2 Esports, Cloud9, 100 Thieves",
                        "employment": "Zor",
                        "salary": "Orta",
                        "whyRecommended": "Oyun oynama yeteneğin, rekabetçi ruhun, liderlik becerin ve ekip çalışması becerilerin,  e-spor oyuncusu olarak başarılı olmanı sağlayabilir.  Hırslı ve motive olman, bu zorlu yolda ilerlemeni sağlayacaktır.",
                        "image": "https://plus.unsplash.com/premium_photo-1661284828052-ea25d6ea94cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "percentage":"%86"
                        },
                        {
                        "name": "Kullanıcı Deneyimi (UX) Tasarımcısı",
                        "description": "UX Tasarımcıları, ürün ve hizmetlerin kullanıcılar için mümkün olduğunca kullanışlı, erişilebilir ve keyifli olmasını sağlar. Araştırma, prototipleme, tasarım ve test gibi süreçleri kullanarak kullanıcı deneyimini optimize ederler.",
                        "companies": "Google, Microsoft, Amazon, Apple, Facebook, Spotify, Airbnb",
                        "employment": "Orta",
                        "salary": "Orta",
                        "whyRecommended": "Teknoloji ve tasarım merakın, hayal gücün ve iletişim becerilerin bu mesleğe uygun olduğunu gösteriyor.  Grafik tasarım, fotoğrafçılık ve araştırma yeteneklerin de UX tasarımcısı olmak için önemli avantajlar sağlayacaktır.",
                        "image": "https://plus.unsplash.com/premium_photo-1661284828052-ea25d6ea94cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "percentage":"%86"
                        },
                        {
                        "name": "Kullanıcı Deneyimi (UX) Tasarımcısı",
                        "description": "UX Tasarımcıları, ürün ve hizmetlerin kullanıcılar için mümkün olduğunca kullanışlı, erişilebilir ve keyifli olmasını sağlar. Araştırma, prototipleme, tasarım ve test gibi süreçleri kullanarak kullanıcı deneyimini optimize ederler.",
                        "companies": "Google, Microsoft, Amazon, Apple, Facebook, Spotify, Airbnb",
                        "employment": "Orta",
                        "salary": "Orta",
                        "whyRecommended": "Teknoloji ve tasarım merakın, hayal gücün ve iletişim becerilerin bu mesleğe uygun olduğunu gösteriyor.  Grafik tasarım, fotoğrafçılık ve araştırma yeteneklerin de UX tasarımcısı olmak için önemli avantajlar sağlayacaktır.",
                        "image": "https://plus.unsplash.com/premium_photo-1661284828052-ea25d6ea94cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "percentage":"%86"
                        },
                        {
                        "name": "Kullanıcı Deneyimi (UX) Tasarımcısı",
                        "description": "UX Tasarımcıları, ürün ve hizmetlerin kullanıcılar için mümkün olduğunca kullanışlı, erişilebilir ve keyifli olmasını sağlar. Araştırma, prototipleme, tasarım ve test gibi süreçleri kullanarak kullanıcı deneyimini optimize ederler.",
                        "companies": "Google, Microsoft, Amazon, Apple, Facebook, Spotify, Airbnb",
                        "employment": "Orta",
                        "salary": "Orta",
                        "whyRecommended": "Teknoloji ve tasarım merakın, hayal gücün ve iletişim becerilerin bu mesleğe uygun olduğunu gösteriyor.  Grafik tasarım, fotoğrafçılık ve araştırma yeteneklerin de UX tasarımcısı olmak için önemli avantajlar sağlayacaktır.",
                        "image": "https://plus.unsplash.com/premium_photo-1661284828052-ea25d6ea94cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "percentage":"%86"
                        }
                        ]

            İşte bilgilerim;

        """.trimIndent(),
    )

    sealed class UiAction {
        data object ReSendPrompt : UiAction()
        data class SelectJob(val job:JobRecommend) : UiAction()
    }

    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data object NavigateToHome : UiEffect()
    }
}