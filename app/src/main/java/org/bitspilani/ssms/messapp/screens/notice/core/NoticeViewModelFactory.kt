package org.bitspilani.ssms.messapp.screens.notice.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.notice.NoticeScreenModule
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepository
import javax.inject.Inject

class NoticeViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var noticeRepository: NoticeRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newNoticeScreenComponent(NoticeScreenModule()).inject(this)
        return NoticeViewModel(noticeRepository) as T
    }
}