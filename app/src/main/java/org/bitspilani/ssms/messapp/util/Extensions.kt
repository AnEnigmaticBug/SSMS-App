package org.bitspilani.ssms.messapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

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