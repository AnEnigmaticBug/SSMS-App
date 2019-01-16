package org.bitspilani.ssms.messapp.screens.grub.grublist.view.model

import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id

data class ViewLayerGrub(
    val id: Id,
    val name: String,
    val organizer: String,
    val date: String,
    val foodOption: String
)