package org.bitspilani.ssms.messapp.screens.notice.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val notices: List<ViewLayerNotice>) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()

    object MoveToLogin : UiOrder()
}