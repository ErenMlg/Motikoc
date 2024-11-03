package com.softcross.motikoc.presentation.jobAssistant

import com.softcross.motikoc.domain.model.ChatItem

object JobAssistantContract {
    data class UiState(
        val isLoading: Boolean = false,
        val originalPrompt:String = "Merhaba senden kariyer asistanı gibi davranmanı istiyorum, sana meslekler hakkında sorular soracağım onları yanıtlamanı istiyorum.",
        val prompt: String = "",
        val chatList: List<ChatItem> = listOf(
            ChatItem(
                "Merhaba! Ben kariyer asistanınız.\nSize nasıl yardımcı olabilirim?",
                false
            )
        ),
        val jobList: List<AssistantJobItem> = emptyList(),
        val selectedJob: AssistantJobItem? = null,
        val isError: Boolean = false
    )

    sealed class UiAction {
        data object SendMessage : UiAction()
        data class OnPromptChanged(val prompt: String) : UiAction()
        data class OnJobSelectionChanged(val job: AssistantJobItem) : UiAction()
        data object OnDoneClicked : UiAction()
    }

    sealed class UiEffect {
        data object NavigateToGoals : UiEffect()
    }
}