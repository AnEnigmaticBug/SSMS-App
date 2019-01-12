package org.bitspilani.ssms.messapp.screens.grub.shared.data.repo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Grub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.threeten.bp.LocalDateTime

interface GrubRepository {

    enum class GrubType {

        Veg, NonVeg
    }

    /**
     * This is Maybe.empty() if the data hasn't been retrieved even once.
     * */
    fun getLastUpdatedDateTime(): Maybe<LocalDateTime>

    /**
     * This makes the repository fetch the grubs data from online source.
     * It completes when the data has been successfully obtained from it.
     * */
    fun forceDataRefresh(): Completable

    fun getAllGrubs(): Observable<List<Grub>>

    fun getGrubById(id: Id): Observable<Grub>

    fun signUpGrubWithId(id: Id, type: GrubType): Completable

    fun cancelGrubWithId(id: Id, type: GrubType): Completable
}