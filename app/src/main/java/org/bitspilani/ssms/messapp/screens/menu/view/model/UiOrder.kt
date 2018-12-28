package org.bitspilani.ssms.messapp.screens.menu.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    data class ShowWorking(val dates: List<ViewLayerDate>, val meals: List<ViewLayerMeal>) : UiOrder()

    data class ShowFailure(val error: String) : UiOrder()

    object MoveToLogin : UiOrder()
}