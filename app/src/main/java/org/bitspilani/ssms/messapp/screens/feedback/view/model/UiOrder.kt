package org.bitspilani.ssms.messapp.screens.feedback.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val feedback: ViewLayerFeedback) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()

    object MoveToLogin : UiOrder()
}