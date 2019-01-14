package org.bitspilani.ssms.messapp.screens.notice.data.repo

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Notice
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority
import org.bitspilani.ssms.messapp.screens.notice.data.retrofit.NoticeService
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.notice.data.room.model.DataLayerNotice
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

class NoticeRepositoryImpl(
    private val prefs: SharedPreferences,
    private val noticesDao: NoticesDao,
    private val networkWatcher: NetworkWatcher,
    private val noticeService: NoticeService
) : NoticeRepository {

    @SuppressLint("CheckResult")
    override fun getAllNotices(): Observable<List<Notice>> {
        return noticesDao.getNoticeCount()
            .take(1)
            .flatMap { _count ->
                when {
                    _count <= 0 -> {
                        when(networkWatcher.isConnectedToInternet()) {
                            false -> throw NoDataSourceException("Not connected to the internet")
                            true  -> Flowable.concat(fetchAndInsertNotices().toFlowable(), noticesDao.getAllNotices())
                        }
                    }
                    else        -> Flowable.merge(fetchAndInsertNotices().toFlowable(), noticesDao.getAllNotices())
                }
            }
            .onErrorResumeNext(noticesDao.getAllNotices())
            .map { _notices ->
                _notices.map { Notice(it.id, it.heading, it.content, it.priority, it.datetime) }
            }
            .subscribeOn(Schedulers.io())
            .toObservable()
    }

    override fun deleteAllNotices(): Completable {
        return Completable.fromAction {
            val newlyDeletedIds = noticesDao.getAllIds().toSet().map { it.toString() }
            val deletedIds = HashSet(prefs.getStringSet("DELETED_IDS", mutableSetOf())!!)
            deletedIds.addAll(newlyDeletedIds)

            prefs.edit(commit = true) {
                putStringSet("DELETED_IDS", deletedIds)
            }

            noticesDao.deleteAllNotices()
        }.subscribeOn(Schedulers.io())
    }

    override fun deleteNoticeById(id: Id): Completable {
        return Completable.fromAction {
            val deletedIds = HashSet(prefs.getStringSet("DELETED_IDS", mutableSetOf())!!)
            deletedIds.add(id.toString())

            prefs.edit(commit = true) {
                putStringSet("DELETED_IDS", deletedIds)
            }

            noticesDao.deleteNoticeById(id)
        }.subscribeOn(Schedulers.io())
    }

    private fun fetchAndInsertNotices(): Single<List<DataLayerNotice>> {
        return noticeService.getNotices()
            .map { _response ->
                when(_response.code()) {
                    200  -> _response.body()!!
                    else -> throw Exception("${_response.code()}: ${_response.errorBody()?.string()}")
                }
            }
            .map { _notices ->
                val deletedIds = prefs.getStringSet("DELETED_IDS", setOf())!!.map { it.toLong() }
                _notices.filter { it.id !in deletedIds }
            }
            .map { _notices ->
                _notices.map {
                    val priority = when(it.priority) {
                        "C"  -> Priority.Critical
                        "I"  -> Priority.Important
                        "N"  -> Priority.Normal
                        else -> throw IllegalArgumentException("Invalid notice priority: ${it.priority}")
                    }

                    val datetime = LocalDateTime.of(LocalDate.parse(it.date), LocalTime.NOON)

                    DataLayerNotice(it.id, it.heading, it.content, priority, datetime)
                }
            }
            .doOnSuccess { _notices ->
                noticesDao.deleteAllNotices()
                noticesDao.insertNotices(_notices)
            }
            .subscribeOn(Schedulers.io())
    }
}