package org.bitspilani.ssms.messapp.screens.notice.data.repo

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Notice
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority
import org.bitspilani.ssms.messapp.screens.notice.data.retrofit.NoticeService
import org.bitspilani.ssms.messapp.screens.notice.data.retrofit.model.NoticeResponse
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.notice.data.room.model.DataLayerNotice
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.bitspilani.ssms.messapp.util.getBody
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.util.*

class NoticeRepositoryImpl(
    private val userRepository: UserRepository,
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
                            false -> throw NoDataSourceException()
                            true  -> fetchAndUpdateNotices().andThen(noticesDao.getAllNotices())
                        }
                    }
                    else        -> Flowable.merge(fetchAndUpdateNotices().toFlowable(), noticesDao.getAllNotices())
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

    private fun fetchAndUpdateNotices(): Completable {
        return userRepository.getUser()
            .flatMapSingle { _user ->
                noticeService.getNotices(_user.jwt)
                    .map { _response ->
                        _response.getBody()
                    }
                    .map { _notices ->
                        val deletedIds = prefs.getStringSet("DELETED_IDS", setOf())!!.map { it.toLong() }
                        _notices.filter { it.id !in deletedIds }
                    }
                    .map { _notices ->
                        _notices.map {
                            it.toDataLayer()
                        }
                    }
                    .doOnSuccess { _notices ->
                        noticesDao.deleteAllNotices()
                        noticesDao.insertNotices(_notices)
                    }
                    .subscribeOn(Schedulers.io())
            }
            .subscribeOn(Schedulers.io())
            .ignoreElement()
    }


    private fun NoticeResponse.toDataLayer(): DataLayerNotice {
        val priority = when(this.priority) {
            "C"  -> Priority.Critical
            "I"  -> Priority.Important
            "N"  -> Priority.Normal
            else -> throw IllegalArgumentException("Invalid notice priority: ${this.priority}")
        }

        val datetime = LocalDateTime.of(LocalDate.parse(this.date), LocalTime.NOON)

        return DataLayerNotice(id, heading, content, priority, datetime)
    }
}