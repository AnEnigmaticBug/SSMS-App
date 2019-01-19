package org.bitspilani.ssms.messapp.di.screens.grub.shared

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepository
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepositoryImpl
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.GrubService
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesDao
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase
import retrofit2.Retrofit

@Module
class GrubScreenModule {

    @Provides
    fun providesGrubRepository(userRepository: UserRepository, grubBatchesDao: GrubBatchesDao, grubService: GrubService): GrubRepository = GrubRepositoryImpl(userRepository, grubBatchesDao, grubService)

    @Provides
    fun providesGrubService(retrofit: Retrofit): GrubService = retrofit.create(GrubService::class.java)

    @Provides
    fun providesGrubBatchesDao(appDatabase: AppDatabase): GrubBatchesDao = appDatabase.grubBatchesDao()
}