package org.bitspilani.ssms.messapp.screens.splash.view.model

sealed class UiOrder {

    object MoveToLogin : UiOrder()

    object MoveToMainApp : UiOrder()
}