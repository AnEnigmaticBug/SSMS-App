package org.bitspilani.ssms.messapp.di.screens.grub.shared

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepository
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepositoryImpl
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.GrubService
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubsDao
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase
import retrofit2.Retrofit

@Module
class GrubScreenModule {

    @Provides
    fun providesGrubRepository(userRepository: UserRepository, grubsDao: GrubsDao, grubService: GrubService): GrubRepository = GrubRepositoryImpl(userRepository, grubsDao, grubService)

    @Provides
    fun providesGrubService(retrofit: Retrofit): GrubService = retrofit.create(GrubService::class.java)

    @Provides
    fun providesGrubBatchesDao(appDatabase: AppDatabase): GrubsDao = appDatabase.grubBatchesDao()
}