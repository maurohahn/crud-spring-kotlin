package me.maurohahn.crudapi.exception

import org.springframework.http.HttpStatus

class NotFoundException : HttpException(HttpStatus.NOT_FOUND, "notFound")