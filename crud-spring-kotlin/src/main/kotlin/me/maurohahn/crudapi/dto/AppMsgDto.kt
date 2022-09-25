package me.maurohahn.crudapi.dto

import java.util.Date

data class AppMsgDto(
    var timestamp: Date? = Date(),
    var code: String? = null,
    var message: String? = null,
    var path: String? = null
)