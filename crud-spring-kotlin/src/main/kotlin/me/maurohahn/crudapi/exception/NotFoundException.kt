package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class NotFoundException(message: String = "notFound") : HttpException(HttpStatus.NOT_FOUND, message)