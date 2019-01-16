package org.bitspilani.ssms.messapp.screens.grub.shared.data.repo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Grub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.GrubDetails
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.threeten.bp.LocalDateTime

interface GrubRepository {

    /**
     * This makes the repository fetch the grubs data from online source.
     * It completes when the data has been successfully obtained from it.
     * */
    fun forceDataRefresh(): Completable

    fun getAllGrubDetails(): Observable<List<GrubDetails>>

    fun getGrubById(id: Id): Observable<Grub>

    /**
     * @param id   specifies which grub to sign-up.
     * @param type  specifies the batch to sign-up.
     * */
    fun signUpGrubWithId(id: Id, type: FoodType): Completable

    /**
     * @param id   specifies which grub to  cancel.
     * */
    fun cancelGrubWithId(id: Id): Completable
}