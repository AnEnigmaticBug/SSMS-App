package org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val grub: ViewLayerGrub) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()

    object MoveToLogin : UiOrder()
}