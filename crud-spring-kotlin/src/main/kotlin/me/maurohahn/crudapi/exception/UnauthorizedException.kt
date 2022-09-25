package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class UnauthorizedException : HttpException(HttpStatus.UNAUTHORIZED, "invalidUsernameOrPassword")