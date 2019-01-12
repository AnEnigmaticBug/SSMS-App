package org.bitspilani.ssms.messapp.screens.grub.shared.core.model

import org.threeten.bp.LocalDateTime

typealias Id = Long

/**
 * Represents a grub. A grub may or may not have both veg and non-veg options.
 * If an option isn't present, it is null.
 *
 * @property cancellationLimit  is the last time by which you can cancel grub.
 * */
data class Grub(
    val id: Id,
    val name: String,
    val organizer: String,
    val datetime: LocalDateTime,
    val cancellationLimit: LocalDateTime,
    val vegBatch: GrubBatch?,
    val nonVegBatch: GrubBatch?,
    val status: Status
)