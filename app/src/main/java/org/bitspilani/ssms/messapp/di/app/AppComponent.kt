package org.bitspilani.ssms.messapp.di.app

import dagger.Component
import org.bitspilani.ssms.messapp.di.screens.login.LoginScreenComponent
import org.bitspilani.ssms.messapp.di.screens.login.LoginScreenModule
import javax.inject.Singleton
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenComponent
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenModule
import org.bitspilani.ssms.messapp.di.screens.notice.NoticeScreenComponent
import org.bitspilani.ssms.messapp.di.screens.notice.NoticeScreenModule
import org.bitspilani.ssms.messapp.di.screens.profile.ProfileScreenComponent
import org.bitspilani.ssms.messapp.di.screens.profile.ProfileScreenModule
import org.bitspilani.ssms.messapp.di.screens.splash.SplashScreenComponent
import org.bitspilani.ssms.messapp.di.screens.splash.SplashScreenModule

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {

    fun newMenuScreenComponent(m1: MenuScreenModule): MenuScreenComponent

    fun newNoticeScreenComponent(m1: NoticeScreenModule): NoticeScreenComponent

    fun newProfileScreenComponent(m1: ProfileScreenModule): ProfileScreenComponent

    fun newLoginScreenComponent(m1: LoginScreenModule): LoginScreenComponent

    fun newSplashScreenComponent(m1: SplashScreenModule): SplashScreenComponent
}