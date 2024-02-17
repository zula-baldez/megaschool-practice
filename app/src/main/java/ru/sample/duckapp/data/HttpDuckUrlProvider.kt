package ru.sample.duckapp.data

interface HttpDuckUrlProvider {
    fun getHttpDuckUrl(code: Int): String
}