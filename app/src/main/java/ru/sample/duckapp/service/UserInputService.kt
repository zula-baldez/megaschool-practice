package ru.sample.duckapp.service

import ru.sample.duckapp.domain.HttpCode

interface UserInputService {
    fun parseAndValidateUserHttpCodeInput(code: String): HttpCode
}