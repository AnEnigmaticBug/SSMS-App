package org.bitspilani.ssms.messapp.screens.grub.shared.core.model

/**
 * Represents a grub. A grub may or may not have both veg and non-veg options.
 * Those options are represented by batches here. If an option is not present,
 * it is null.
 * */
data class Grub(val details: GrubDetails, val vegBatch: GrubBatch?, val nonVegBatch: GrubBatch?)