package org.bitspilani.ssms.messapp.di.screens.grub.shared

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepository
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepositoryImpl
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesDao
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase

@Module
class GrubScreenModule {

    @Provides
    fun providesGrubRepository(grubBatchesDao: GrubBatchesDao): GrubRepository = GrubRepositoryImpl(grubBatchesDao)

    @Provides
    fun providesGrubBatchesDao(appDatabase: AppDatabase): GrubBatchesDao = appDatabase.grubBatchesDao()
}