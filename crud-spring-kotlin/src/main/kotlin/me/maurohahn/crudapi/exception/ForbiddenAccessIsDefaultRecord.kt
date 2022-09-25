package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class ForbiddenAccessIsDefaultRecord : HttpException(HttpStatus.FORBIDDEN, "forbiddenAccessIsDefaultRecord")