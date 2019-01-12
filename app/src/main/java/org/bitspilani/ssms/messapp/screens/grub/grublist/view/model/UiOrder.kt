package org.bitspilani.ssms.messapp.screens.grub.grublist.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val grubs: List<ViewLayerGrub>, val viewOnlySigned: Boolean) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()

    object MoveToLogin : UiOrder()
}