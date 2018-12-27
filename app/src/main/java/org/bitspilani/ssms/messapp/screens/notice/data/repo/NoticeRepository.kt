package org.bitspilani.ssms.messapp.screens.notice.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Notice

interface NoticeRepository {

    fun getAllNotices(): Observable<List<Notice>>

    fun deleteAllNotices(): Completable

    fun deleteNoticeById(id: Id): Completable
}