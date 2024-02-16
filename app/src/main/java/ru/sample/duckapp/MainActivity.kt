package ru.sample.duckapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api

class MainActivity : AppCompatActivity() {
    private lateinit var nextButton: Button
    private lateinit var duckImageView: ImageView

    private val ducksApi = Api.ducksApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        duckImageView = findViewById(R.id.duckImageView)
        nextButton = findViewById(R.id.nextButton)

        nextButton.setOnClickListener {
            fetchRandomDuckImage()
        }
    }

    private fun fetchRandomDuckImage() {
        try {
            getRandomDuckAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRandomDuckAsync() {
        ducksApi.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful) {
                    val duck = response.body()
                    duck?.let {
                        loadImage(it.url)
                    }
                } else {
                    // Handle unsuccessful response here
                    // For example: show an error message
                    // toast("Failed to fetch duck image")
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                // Handle failure here
                // For example: show an error message
                // toast("Network error: ${t.message}")
            }
        })
    }

    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl).into(duckImageView)
    }
}
