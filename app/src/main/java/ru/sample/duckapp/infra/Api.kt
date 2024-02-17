package ru.sample.duckapp.infra

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.sample.duckapp.data.DucksApi
import ru.sample.duckapp.data.HttpDuckUrlProvider

object Api : HttpDuckUrlProvider {
    private const val BASE_URL = "https://random-d.uk/api/v2/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val ducksApi by lazy {
        retrofit.create(DucksApi::class.java)
    }

    override fun getHttpDuckUrl(code: Int): String {
        print(BASE_URL.plus(code))
        return BASE_URL.plus("http/$code")
    }
}