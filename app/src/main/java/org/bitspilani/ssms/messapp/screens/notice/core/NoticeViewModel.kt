package org.bitspilani.ssms.messapp.screens.notice.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepository
import org.bitspilani.ssms.messapp.screens.notice.view.model.ViewLayerNotice
import org.bitspilani.ssms.messapp.util.set
import org.bitspilani.ssms.messapp.util.toMut
import org.threeten.bp.LocalDateTime

class NoticeViewModel(private val nRepo: NoticeRepository) : ViewModel() {

    val notices: LiveData<List<ViewLayerNotice>> = MutableLiveData()


    private val d1 = CompositeDisposable()


    init {
        d1.set(nRepo.getAllNotices()
            .subscribe(
                { _notices ->
                    notices.toMut().postValue(_notices.sortedByDescending { it.datetime }.map {
                        ViewLayerNotice(
                            it.id,
                            it.heading,
                            it.content,
                            it.priority,
                            it.datetime.prettyDate(),
                            it.datetime.prettyTime()
                        )
                    })
                },
                {

                }
            ))
    }


    fun onDeleteNoticeByIdAction(id: Id) {
        nRepo.deleteNoticeById(id).subscribe()
    }

    fun onDeleteAllNoticesAction() {
        nRepo.deleteAllNotices().subscribe()
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