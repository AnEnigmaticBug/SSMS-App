package org.bitspilani.ssms.messapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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