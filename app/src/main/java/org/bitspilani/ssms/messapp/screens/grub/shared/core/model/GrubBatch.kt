package org.bitspilani.ssms.messapp.screens.grub.shared.core.model

/**
 * Represents a batch of a grub. There can be two batches: veg or non-veg.
 * */
data class GrubBatch(
    val menu: Set<String>,
    val venue: String = "TBA",
    val price: Int
)