package org.bitspilani.ssms.messapp.screens.contact.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val contacts: List<ViewLayerContact>) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()
}