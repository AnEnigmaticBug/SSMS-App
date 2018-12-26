package org.bitspilani.ssms.messapp.screens.notice.core.model

import org.threeten.bp.LocalDateTime

typealias Id = Long

data class Notice(
    val id: Id,
    val heading: String,
    val content: String = "",
    val priority: Priority = Priority.Normal,
    val datetime: LocalDateTime = LocalDateTime.now()
)