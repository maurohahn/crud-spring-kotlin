package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class InvalidUsernameOrPassword : HttpException(HttpStatus.UNAUTHORIZED, "invalidUsernameOrPassword")