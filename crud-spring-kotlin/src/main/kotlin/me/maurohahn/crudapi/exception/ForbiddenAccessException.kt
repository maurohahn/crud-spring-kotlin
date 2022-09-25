package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class ForbiddenAccessException : HttpException(HttpStatus.FORBIDDEN, "forbiddenAccess")