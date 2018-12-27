package org.bitspilani.ssms.messapp.screens.notice.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Notice
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.notice.data.room.model.DataLayerNotice
import org.threeten.bp.LocalDateTime

class NoticeRepositoryImpl(private val noticesDao: NoticesDao) : NoticeRepository {

    init {
        Completable.fromAction {
            noticesDao.deleteAllNotices()
            noticesDao.insertNotices(
                listOf(
                    DataLayerNotice(1, "Change in dinner timings", "Yeah. Sad but true", Priority.Important, LocalDateTime.now()),
                    DataLayerNotice(2, "Release of SSMS App", "Now, that's awesome", Priority.Critical, LocalDateTime.now().minusDays(3)),
                    DataLayerNotice(3, "Lorem Ipsum", "Dolor sit amet", Priority.Normal, LocalDateTime.now().minusDays(1)),
                    DataLayerNotice(4, "Mess Food Wastage", "12kg of food was wasted", Priority.Important, LocalDateTime.now().minusDays(1)),
                    DataLayerNotice(5, "Bolo tara rara", "Perty ytreP", Priority.Normal, LocalDateTime.now().minusWeeks(1)),
                    DataLayerNotice(6, "Generic Heading", "Generic Content", Priority.Normal, LocalDateTime.now().minusMonths(1))
                )
            )
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun getAllNotices(): Observable<List<Notice>> {
        return noticesDao.getAllNotices()
            .subscribeOn(Schedulers.io())
            .map { it.map { Notice(it.id, it.heading, it.content, it.priority, it.datetime) } }
            .toObservable()
    }

    override fun deleteAllNotices(): Completable {
        return Completable.fromAction {
            noticesDao.deleteAllNotices()
        }.subscribeOn(Schedulers.io())
    }

    override fun deleteNoticeById(id: Id): Completable {
        return Completable.fromAction {
            noticesDao.deleteNoticeById(id)
        }.subscribeOn(Schedulers.io())
    }
}