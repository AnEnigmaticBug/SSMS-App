package org.bitspilani.ssms.messapp.screens.notice.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepository
import org.bitspilani.ssms.messapp.screens.notice.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.notice.view.model.ViewLayerNotice
import org.bitspilani.ssms.messapp.util.*
import org.threeten.bp.LocalDateTime

class NoticeViewModel(private val nRepo: NoticeRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = SingleLiveEvent()


    private val d1 = CompositeDisposable()
    private val d2 = CompositeDisposable()


    init {
        initialize()
    }


    fun onRetryAction() {
        initialize()
    }

    fun onDeleteNoticeByIdAction(id: Id) {
        d2.set(nRepo.deleteNoticeById(id)
            .subscribe(
                {

                },
                {
                    toast.toMut().postValue(it.getMessage())
                }
            ))
    }

    fun onDeleteAllNoticesAction() {
        d2.set(nRepo.deleteAllNotices()
            .subscribe(
                {

                },
                {
                    toast.toMut().postValue(it.getMessage())
                }
            ))
    }


    private fun initialize() {
        order.toMut().value = UiOrder.ShowLoading
        toast.toMut().value = null
        d1.set(nRepo.getAllNotices()
            .subscribe(
                { _notices ->
                    order.toMut().postValue(UiOrder.ShowWorking(_notices.sortedByDescending { it.datetime }.map {
                        ViewLayerNotice(
                            it.id,
                            it.heading,
                            it.content,
                            it.priority,
                            it.datetime.prettyDate(),
                            it.datetime.prettyTime()
                        )
                    }))
                },
                {
                    order.toMut().postValue(UiOrder.ShowFailure(it.getMessage()))
                }
            ))
    }


    private fun LocalDateTime.prettyDate(): String {
        val month = this.month.toString().take(3).toLowerCase().capitalize()
        return "$month ${this.dayOfMonth}"
    }

    private fun LocalDateTime.prettyTime(): String {
        val hour = if(this.hour < 12) { this.hour } else { this.hour - 12 }
        val ampm = if(this.hour < 12) { "AM" } else { "PM" }
        return "$hour:$minute $ampm"
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
    }
}