package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

abstract class HttpException(val status: HttpStatus, message: String) : Exception(message)