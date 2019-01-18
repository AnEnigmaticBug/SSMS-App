package org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.model

import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.SigningStatus
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Slot

data class ViewLayerGrub(
    val name: String,
    val organizer: String,
    val foodOption: FoodOption,
    val date: String,
    val slot1Time: String,
    val slot2Time: String,
    val signUpDeadline: String,
    val cancelDeadline: String,
    val vegPrice: String = "TBA",
    val nonVegPrice: String = "TBA",
    val vegVenue: String = "TBA",
    val nonVegVenue: String = "TBA",
    val vegMenu: Set<String> = setOf(),
    val nonVegMenu: Set<String> = setOf(),
    val signingStatus: SigningStatus,
    val slot: String = "N/A"
)