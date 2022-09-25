package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class InvalidArgumentsException : HttpException(HttpStatus.BAD_REQUEST, "invalidArguments")