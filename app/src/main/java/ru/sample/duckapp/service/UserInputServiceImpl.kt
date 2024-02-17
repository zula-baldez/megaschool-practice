package ru.sample.duckapp.service

import ru.sample.duckapp.domain.HttpCode

class UserInputServiceImpl : UserInputService {
    override fun parseAndValidateUserHttpCodeInput(code: String): HttpCode {
        return try {
            val httpCode = code.toInt()
            HttpCode(httpCode, isHttpStatusCode(httpCode))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            HttpCode(-1, false)
        }
    }

    private fun isHttpStatusCode(code: Int): Boolean {
        return code in 100..599
    }
}