package org.bitspilani.ssms.messapp.screens.profile.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val user: ViewLayerUser) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()

    object MoveToLogin : UiOrder()
}