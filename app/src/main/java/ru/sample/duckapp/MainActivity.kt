package ru.sample.duckapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api

class MainActivity : AppCompatActivity() {
    private lateinit var duckImageView: ImageView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var inputField: EditText
    private lateinit var nextButton: Button
    private var https =
        intArrayOf(100, 200, 301, 302, 400, 403, 404, 409, 413, 418, 420, 426, 429, 451, 500)
    private val ducksApi = Api.ducksApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        duckImageView = findViewById(R.id.duckImageView)
        nextButton = findViewById(R.id.nextButton)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        inputField = findViewById(R.id.inputField)

        nextButton.setOnClickListener {
            fetchRandomDuckImage()
        }
    }

    private fun fetchRandomDuckImage() {
        val inputText = inputField.text.toString()
        if (inputText == "") {
            getRandomDuckAsync()
        } else {
            val code = Integer.parseInt(inputText)
            if (validateInput(code)) {
                getCodeDuck(code)
            }
        }
    }

    private fun validateInput(code: Int): Boolean {
        val validation = https.contains(code)
        if (!validation) {
            showToast("Invalid http code!")
        }
        return validation
    }

    private fun getCodeDuck(code: Int) {
        loadingProgressBar.visibility = View.VISIBLE
        duckImageView.visibility = View.GONE
        loadImage("https://random-d.uk/api/v2/http/$code")
    }


    private fun getRandomDuckAsync() {
        loadingProgressBar.visibility = View.VISIBLE
        duckImageView.visibility = View.GONE
        ducksApi.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful) {
                    val duck = response.body()
                    duck?.let {
                        loadImage(it.url)
                    }
                } else {
                    loadingProgressBar.visibility = View.GONE
                    duckImageView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                loadingProgressBar.visibility = View.GONE
                showToast("Error while getting duck")
            }
        })
    }

    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl).into(duckImageView, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                loadingProgressBar.visibility = View.GONE
                duckImageView.visibility = View.VISIBLE
            }

            override fun onError(e: Exception?) {
                loadingProgressBar.visibility = View.GONE
                duckImageView.visibility = View.GONE
                showToast("Service is unavailable!")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
