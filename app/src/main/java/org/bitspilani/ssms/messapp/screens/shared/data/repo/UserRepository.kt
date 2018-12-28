package org.bitspilani.ssms.messapp.screens.shared.data.repo

import io.reactivex.Completable
import io.reactivex.Maybe
import org.bitspilani.ssms.messapp.screens.shared.core.model.User

/**
 * The repository meant to access/modify user related information.
 * */
interface UserRepository {

    fun getUser(): Maybe<User>

    fun setUser(user: User?): Completable

    fun refreshQrCode(): Completable
}