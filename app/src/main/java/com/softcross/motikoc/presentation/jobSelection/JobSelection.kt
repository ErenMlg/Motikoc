package com.softcross.motikoc.presentation.jobSelection

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.softcross.motikoc.R
import com.softcross.motikoc.domain.model.JobRecommend
import com.softcross.motikoc.presentation.components.FilledButton
import com.softcross.motikoc.presentation.components.MotikocAsyncImage
import com.softcross.motikoc.presentation.components.MotikocHeader
import com.softcross.motikoc.presentation.components.MotikocLottieAnimation
import com.softcross.motikoc.presentation.components.MotikocSnackbar
import com.softcross.motikoc.presentation.components.SnackbarType
import com.softcross.motikoc.presentation.jobSelection.JobSelectionContract.UiAction
import com.softcross.motikoc.presentation.jobSelection.JobSelectionContract.UiEffect
import com.softcross.motikoc.presentation.jobSelection.JobSelectionContract.UiState
import com.softcross.motikoc.presentation.theme.BackgroundColor
import com.softcross.motikoc.presentation.theme.DarkBackground
import com.softcross.motikoc.presentation.theme.DarkSurface
import com.softcross.motikoc.presentation.theme.LightBackground
import com.softcross.motikoc.presentation.theme.MotikocTheme
import com.softcross.motikoc.presentation.theme.PoppinsBold
import com.softcross.motikoc.presentation.theme.PoppinsLight
import com.softcross.motikoc.presentation.theme.PoppinsMedium
import com.softcross.motikoc.presentation.theme.TextColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun JobSelection(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    onAction: (UiAction) -> Unit,
    navigateToAssistant: (JobRecommend) -> Unit,
    navigateToHome: () -> Unit
) {
    var errorMessage by remember { mutableStateOf("") }
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is UiEffect.NavigateToHome -> {
                        navigateToHome()
                    }

                    is UiEffect.ShowSnackbar -> {
                        errorMessage = effect.message
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                MotikocLottieAnimation(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(DarkSurface.copy(0.5f)),
                )
                Text(
                    text = "Size en uygun meslekleri bulmaya çalışıyoruz...",
                    fontFamily = PoppinsLight,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = TextColor,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            } else if (errorMessage.isNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.icon_error),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(DarkSurface.copy(0.5f)),
                )
                Text(
                    text = "Bir hata oluştu. Lütfen tekrar deneyin.",
                    fontFamily = PoppinsLight,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = TextColor,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
                FilledButton(
                    text = "Tekrar dene",
                    modifier = Modifier,
                    onClick = { onAction(UiAction.TryAgain) }
                )
            }else{
                JobSelectionContent(
                    jobRecommendations = uiState.jobRecommendList,
                    onReRecommend = { onAction(UiAction.ReSendPrompt) },
                    onAssistantClick = navigateToAssistant,
                    onSelectJob = { onAction(UiAction.SelectJob(it)) }
                )
            }
        }
    }

}

@Composable
fun JobSelectionContent(
    jobRecommendations: List<JobRecommend>,
    onReRecommend: () -> Unit,
    onAssistantClick: (JobRecommend) -> Unit,
    onSelectJob: (JobRecommend) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { jobRecommendations.size }, initialPage = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            Modifier
                .fillMaxHeight(0.85f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MotikocHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                title = R.string.job_wizard,
                subtitle = R.string.job_recommend_result
            )
            HorizontalPager(
                state = pagerState
            ) { page ->
                val currentJob = jobRecommendations[page]
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                    {
                        MotikocAsyncImage(
                            model = currentJob.image,
                            errorIcon = R.drawable.job_background,
                            contentDescription = "",
                            alignment = Alignment.Center,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .drawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                DarkBackground.copy(0.9f),
                                            ),
                                            startY = 0f,
                                            endY = 480f
                                        )
                                    )
                                }
                                .fillMaxWidth(0.95f)
                                .height(210.dp)
                                .align(Alignment.Center)
                        )
                        IconButton(
                            onClick = { onAssistantClick(currentJob) },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 32.dp, bottom = 16.dp)
                                .background(
                                    color = LightBackground,
                                    shape = CircleShape
                                )
                                .size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ai_question),
                                tint = DarkBackground,
                                contentDescription = "",
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = currentJob.name,
                            color = LightBackground,
                            fontFamily = PoppinsMedium,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(start = 32.dp, bottom = 16.dp)
                                .align(Alignment.BottomStart),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .height(180.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "Bu meslek ne yapar?",
                            color = TextColor,
                            fontFamily = PoppinsBold
                        )
                        Text(
                            text = currentJob.description,
                            overflow = TextOverflow.Ellipsis,
                            color = TextColor,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .height(180.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "Neden uygun?",
                            color = TextColor,
                            fontFamily = PoppinsBold
                        )
                        Text(
                            text = currentJob.whyRecommended,
                            overflow = TextOverflow.Ellipsis,
                            color = TextColor
                        )
                    }
                    Column(
                        modifier = Modifier
                            .height(100.dp)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "Çalışabileceğin yerler?",
                            color = TextColor,
                            fontFamily = PoppinsBold
                        )
                        Text(
                            text = currentJob.companies,
                            color = TextColor
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "Ortalama maaşlar nedir : ",
                            color = TextColor,
                            fontFamily = PoppinsBold
                        )
                        Text(text = currentJob.salary, color = TextColor)
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = "İş imkanı ne durumda : ",
                            color = TextColor,
                            fontFamily = PoppinsBold
                        )
                        Text(text = currentJob.employment, color = TextColor)
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledButton(
                text = "Tekrar öner",
                modifier = Modifier.weight(0.1f),
                onClick = onReRecommend
            )
            FilledButton(
                text = "Bu mesleği seç",
                modifier = Modifier.weight(0.1f),
                onClick = { onSelectJob(jobRecommendations[pagerState.currentPage]) })
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JobSelectionPreviewDark() {
    MotikocTheme {
        JobSelection(
            uiState = UiState(
                jobRecommendList = listOf(
                    JobRecommend(
                        id = 1,
                        name = "Kullanıcı Deneyimi (UX) Tasarımcısı",
                        description = "UX Tasarımcıları, ürün ve hizmetlerin kullanıcılar için mümkün olduğunca kullanışlı, erişilebilir ve keyifli olmasını sağlar. Araştırma, prototipleme, tasarım ve test gibi süreçleri kullanarak kullanıcı deneyimini optimize ederler.",
                        companies = "Google, Microsoft, Amazon, Apple, Facebook, Spotify, Airbnb",
                        employment = "Orta",
                        salary = "Orta",
                        whyRecommended = "Teknoloji ve tasarım merakın, hayal gücün ve iletişim becerilerin bu mesleğe uygun olduğunu gösteriyor.  Grafik tasarım, fotoğrafçılık ve araştırma yeteneklerin de UX tasarımcısı olmak için önemli avantajlar sağlayacaktır.",
                        image = "https://images.unsplash.com/photo-1653669487003-7d89b2020f3c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8am9ifGVufDB8fDB8fHww"
                    ),
                    JobRecommend(
                        id = 2,
                        name = "E-Spor Oyuncusu",
                        description = "E-Spor Oyuncuları, belirli video oyunlarında profesyonel olarak yarışırlar.  Takım çalışması, stratejik düşünme, hızlı refleksler ve sürekli pratik yapma becerileri gerektirir. Turnuvalara katılarak ödüller kazanırlar.",
                        companies = "Riot Games, Fnatic, Team Liquid, G2 Esports, Cloud9, 100 Thieves",
                        employment = "Zor",
                        salary = "Orta",
                        whyRecommended = "Oyun oynama yeteneğin, rekabetçi ruhun, liderlik becerin ve ekip çalışması becerilerin,  e-spor oyuncusu olarak başarılı olmanı sağlayabilir.  Hırslı ve motive olman, bu zorlu yolda ilerlemeni sağlayacaktır.",
                        image = "https://images.unsplash.com/photo-1499750310107-5fef28a66643?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8am9ifGVufDB8fDB8fHww"
                    )
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToAssistant = {},
            navigateToHome = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun JobSelectionPreviewLight() {
    MotikocTheme {
        JobSelection(
            uiState = UiState(
                jobRecommendList = listOf(
                    JobRecommend(
                        id = 1,
                        name = "Kullanıcı Deneyimi (UX) Tasarımcısı",
                        description = "UX Tasarımcıları, ürün ve hizmetlerin kullanıcılar için mümkün olduğunca kullanışlı, erişilebilir ve keyifli olmasını sağlar. Araştırma, prototipleme, tasarım ve test gibi süreçleri kullanarak kullanıcı deneyimini optimize ederler.",
                        companies = "Google, Microsoft, Amazon, Apple, Facebook, Spotify, Airbnb",
                        employment = "Orta",
                        salary = "Orta",
                        whyRecommended = "Teknoloji ve tasarım merakın, hayal gücün ve iletişim becerilerin bu mesleğe uygun olduğunu gösteriyor.  Grafik tasarım, fotoğrafçılık ve araştırma yeteneklerin de UX tasarımcısı olmak için önemli avantajlar sağlayacaktır.",
                        image = "https://images.unsplash.com/photo-1653669487003-7d89b2020f3c?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8am9ifGVufDB8fDB8fHww"
                    ),
                    JobRecommend(
                        id = 2,
                        name = "E-Spor Oyuncusu",
                        description = "E-Spor Oyuncuları, belirli video oyunlarında profesyonel olarak yarışırlar.  Takım çalışması, stratejik düşünme, hızlı refleksler ve sürekli pratik yapma becerileri gerektirir. Turnuvalara katılarak ödüller kazanırlar.",
                        companies = "Riot Games, Fnatic, Team Liquid, G2 Esports, Cloud9, 100 Thieves",
                        employment = "Zor",
                        salary = "Orta",
                        whyRecommended = "Oyun oynama yeteneğin, rekabetçi ruhun, liderlik becerin ve ekip çalışması becerilerin,  e-spor oyuncusu olarak başarılı olmanı sağlayabilir.  Hırslı ve motive olman, bu zorlu yolda ilerlemeni sağlayacaktır.",
                        image = "https://images.unsplash.com/photo-1499750310107-5fef28a66643?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8am9ifGVufDB8fDB8fHww"
                    )
                )
            ),
            uiEffect = emptyFlow(),
            onAction = {},
            navigateToAssistant = {},
            navigateToHome = {}
        )
    }
}