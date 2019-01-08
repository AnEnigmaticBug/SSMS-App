package org.bitspilani.ssms.messapp.screens.login.view.model

sealed class UiOrder {

    object ShowLoading : UiOrder()

    object ShowWorking : UiOrder()

    object MoveToMainApp : UiOrder()
}