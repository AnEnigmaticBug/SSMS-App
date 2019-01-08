package org.bitspilani.ssms.messapp.screens.shared.data.repo

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.shared.core.model.User
import java.util.*
import java.util.concurrent.TimeUnit

class UserRepositoryImpl(private val prefs: SharedPreferences) : UserRepository {

    object Keys {

        const val id = "ID"
        const val name = "NAME"
        const val room = "ROOM"
        const val profilePicUrl = "PROFILE_PIC_URL"
        const val qrCode = "QR"
        const val jwt = "JWT"
    }

    override fun login(idToken: String, profilePicUrl: String): Completable {
        return Completable.fromAction {
            setUser(User("2017A6PS0666P", "Creeping Monster", "MR 2108", profilePicUrl, "lkjgs;lkjg;qkj;lkjl;3lk4ljdkljalkdgj;ldkjg;ldjkg;aldjsgalkdsjgal;kjg;k", "JWT lol"))
                .subscribe()
        }.delay(3, TimeUnit.SECONDS)
    }

    override fun getUser(): Maybe<User> {
        val id = prefs.getString(Keys.id, null)
        val name = prefs.getString(Keys.name, null)
        val room = prefs.getString(Keys.room, null)
        val profilePicUrl = prefs.getString(Keys.profilePicUrl, null)
        val qrCode = prefs.getString(Keys.room, null)
        val jwt = prefs.getString(Keys.room, null)

        if(listOf(id, name, room, profilePicUrl, qrCode, jwt).contains(null)) {
            return Maybe.empty()
        }

        return Maybe.just(User(id!!, name!!, room!!, profilePicUrl!!, qrCode!!, jwt!!))
    }

    override fun setUser(user: User?): Completable {
        return Completable.fromAction {
            prefs.edit(commit = true) {
                putString(Keys.id, user?.id)
                putString(Keys.name, user?.name)
                putString(Keys.room, user?.room)
                putString(Keys.profilePicUrl, user?.profilePicUrl)
                putString(Keys.qrCode, user?.qrCode)
                putString(Keys.jwt, user?.jwt)
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun refreshQrCode(): Completable {
        return Completable.fromAction {
            if(getUser().isEmpty.blockingGet() == true) {
                throw IllegalStateException("Can't refresh token for a non-logged in user")
            }

            prefs.edit(commit = true) {
                putString(Keys.qrCode, UUID.randomUUID().toString().substring(0..30))
            }
        }.subscribeOn(Schedulers.io())
    }
}