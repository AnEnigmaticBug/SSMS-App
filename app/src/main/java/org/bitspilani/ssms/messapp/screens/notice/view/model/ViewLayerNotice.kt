package org.bitspilani.ssms.messapp.screens.notice.view.model

import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority

data class ViewLayerNotice(
    val id: Id,
    val heading: String,
    val content: String,
    val priority: Priority,
    val date: String,
    val time: String
)