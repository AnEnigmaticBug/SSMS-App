package org.bitspilani.ssms.messapp.di.screens.notice

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.notice.core.NoticeViewModelFactory

@Subcomponent(modules = [NoticeScreenModule::class])
interface NoticeScreenComponent {

    fun inject(factory: NoticeViewModelFactory)
}