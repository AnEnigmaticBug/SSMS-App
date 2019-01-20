package org.bitspilani.ssms.messapp.screens.shared.data.repo

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.shared.core.model.User
import org.bitspilani.ssms.messapp.screens.shared.data.retrofit.UserService
import org.bitspilani.ssms.messapp.screens.shared.data.retrofit.model.UserResponse
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoConnectionException
import org.bitspilani.ssms.messapp.util.NoLoggedUserException
import org.bitspilani.ssms.messapp.util.toRequestBody
import org.json.JSONObject

class UserRepositoryImpl(
    private val prefs: SharedPreferences,
    private val networkWatcher: NetworkWatcher,
    private val userService: UserService
) : UserRepository {

    object Keys {

        const val id = "ID"
        const val name = "NAME"
        const val room = "ROOM"
        const val profilePicUrl = "PROFILE_PIC_URL"
        const val qrCode = "QR"
        const val jwt = "JWT"
    }

    override fun login(idToken: String, profilePicUrl: String): Completable {

        fun UserResponse.toUser(): User {
            return User(id, name, roomNo, profilePicUrl, qrCode, jwt)
        }

        if(!networkWatcher.isConnectedToInternet()) {
            return Completable.error(NoConnectionException("Not connected to the internet"))
        }

        val body = JSONObject().also { it.put("id_token", idToken) }.toRequestBody()
        return userService.login(body)
            .map { _response ->
                Log.d("UserRepositoryImpl", "${_response.code()}: ${_response.errorBody()?.string()}")
                when(_response.code()) {
                    200  -> _response.body()!!.toUser()
                    else -> throw Exception("${_response.code()}")
                }
            }
            .doOnSuccess { _user ->
                setUser(_user).subscribe()
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }

    override fun getUser(): Maybe<User> {
        val id = prefs.getString(Keys.id, null)
        val name = prefs.getString(Keys.name, null)
        val room = prefs.getString(Keys.room, null)
        val profilePicUrl = prefs.getString(Keys.profilePicUrl, null)
        val qrCode = prefs.getString(Keys.qrCode, null)
        val jwt = prefs.getString(Keys.jwt, null)

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
        if(!networkWatcher.isConnectedToInternet()) {
            return Completable.error(NoConnectionException("Not connected to the internet"))
        }
        return getUser()
            .switchIfEmpty(Single.error(NoLoggedUserException("User isn't logged in")))
            .flatMap { _user ->
                userService.refreshQrCode(_user.jwt)
            }
            .map { _response ->
                Log.d("UserRepositoryImpl", "${_response.code()}: ${_response.errorBody()?.string()}")
                when(_response.code()) {
                    200  -> _response.body()!!.qrCode
                    else -> throw Exception("${_response.code()}")
                }
            }
            .doOnSuccess { _qrCode ->
                prefs.edit(commit = true) {
                    putString(Keys.qrCode, _qrCode)
                }
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }
}