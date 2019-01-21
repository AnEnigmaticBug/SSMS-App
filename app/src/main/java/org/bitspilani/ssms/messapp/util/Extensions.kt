package org.bitspilani.ssms.messapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException

fun CompositeDisposable.set(disposable: Disposable) {
    clear()
    add(disposable)
}

fun<T> LiveData<T>.toMut(): MutableLiveData<T> {
    return when(this) {
        is MutableLiveData -> this
        else               -> throw IllegalArgumentException("Not a MutableLiveData")
    }
}

fun<T> List<T>.modifyElement(condition: (elem: T) -> Boolean, action: (elem: T) -> T): List<T> {
    val i = this.indexOfFirst { condition.invoke(it) }
    return this.toMutableList().also { _mutableList ->
        _mutableList[i] = action.invoke(_mutableList[i])
    }
}

fun JSONObject.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), this.toString())
}

fun Int.makeTwoDigit(): String {
    return when(this) {
        in 0..9   -> "0$this"
        in 10..99 -> this.toString()
        else      -> throw IllegalArgumentException("Can't make $this into 2 digits")
    }
}

fun<T> Response<T>.getBody(): T {
    return when(code()) {
        200         -> body()!!
        in 400..499 -> throw ErrorCode4XXException(errorBody()!!.string().extract4XXMessage())
        else        -> throw ErrorCode5XXException()
    }
}

fun String.extract4XXMessage(): String {
    val json = JSONObject(this)
    return json.getString("message")
}

fun Throwable.getMessage(): String {
    return when(this) {
        is NoConnectionException  -> "Device isn't connected to the internet"
        is NoLoggedUserException  -> "User isn't logged in or has been logged out"
        is NoDataSourceException  -> "Couldn't get the data to display"
        is ErrorCode4XXException  -> this.message!!
        is ErrorCode5XXException  -> "An issue occurred at the server"
        is UnknownStateException  -> "An error occurred. Please restart the app and try again"
        is SocketTimeoutException -> "Took too long to contact the server"
        else                      -> "Something went wrong :("
    }
}