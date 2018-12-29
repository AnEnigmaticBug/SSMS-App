package org.bitspilani.ssms.messapp.di.app

import dagger.Component
import javax.inject.Singleton
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenComponent
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenModule
import org.bitspilani.ssms.messapp.di.screens.notice.NoticeScreenComponent
import org.bitspilani.ssms.messapp.di.screens.notice.NoticeScreenModule
import org.bitspilani.ssms.messapp.di.screens.profile.ProfileScreenComponent
import org.bitspilani.ssms.messapp.di.screens.profile.ProfileScreenModule

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {

    fun newMenuScreenComponent(m1: MenuScreenModule): MenuScreenComponent

    fun newNoticeScreenComponent(m1: NoticeScreenModule): NoticeScreenComponent

    fun newProfileScreenComponent(m1: ProfileScreenModule): ProfileScreenComponent
}