package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class InvalidArgumentsException(message: String = "invalidArguments") : HttpException(HttpStatus.BAD_REQUEST, message)