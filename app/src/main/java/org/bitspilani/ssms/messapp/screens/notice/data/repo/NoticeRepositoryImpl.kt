package org.bitspilani.ssms.messapp.screens.notice.data.repo

import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.notice.core.model.Notice
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority
import org.threeten.bp.LocalDateTime

class NoticeRepositoryImpl : NoticeRepository {

    override fun getAllNotices(): Observable<List<Notice>> {
        return Observable.just(listOf(
            Notice(1, "Change in dinner timings", "Yeah. Sad but true", Priority.Important, LocalDateTime.now()),
            Notice(2, "Release of SSMS App", "Now, that's awesome", Priority.Critical, LocalDateTime.now().minusDays(3)),
            Notice(3, "Lorem Ipsum", "Dolor sit amet", Priority.Normal, LocalDateTime.now().minusDays(1)),
            Notice(4, "Mess Food Wastage", "12kg of food was wasted", Priority.Important, LocalDateTime.now().minusDays(1)),
            Notice(5, "Bolo tara rara", "Perty ytreP", Priority.Normal, LocalDateTime.now().minusWeeks(1)),
            Notice(6, "Generic Heading", "Generic Content", Priority.Normal, LocalDateTime.now().minusMonths(1))
        ))
    }
}