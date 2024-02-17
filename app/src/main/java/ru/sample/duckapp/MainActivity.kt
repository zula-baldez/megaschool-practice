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
import ru.sample.duckapp.data.HttpDuckUrlProvider
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api
import ru.sample.duckapp.service.UserInputService
import ru.sample.duckapp.service.UserInputServiceImpl

class MainActivity : AppCompatActivity() {
    private lateinit var duckImageView: ImageView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var inputField: EditText
    private lateinit var nextButton: Button
    private val ducksApi = Api.ducksApi
    private val validationService: UserInputService = UserInputServiceImpl()
    private val httpDuckUrlProvider: HttpDuckUrlProvider = Api
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
            val httpCode = validationService.parseAndValidateUserHttpCodeInput(inputText)
            if (!httpCode.isValid) {
                showToast("Invalid http code!")
            } else {
                getCodeDuck(httpCode.code)
            }
        }
    }

    private fun getCodeDuck(code: Int) {
        switchToLoadScreen()
        loadImage(httpDuckUrlProvider.getHttpDuckUrl(code))
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
                    switchToImageScreen()
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                switchToImageScreen()
                setErrorScreen()
                showToast("Error while getting duck")
            }
        })
    }

    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl).into(duckImageView, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                switchToImageScreen()
            }

            override fun onError(e: Exception?) {
                switchToImageScreen()
                setErrorScreen()
                showToast("Service did not returned your duck(!")
            }
        })
    }

    private fun switchToLoadScreen() {
        loadingProgressBar.visibility = View.VISIBLE
        duckImageView.visibility = View.GONE
    }

    private fun switchToImageScreen() {
        loadingProgressBar.visibility = View.GONE
        duckImageView.visibility = View.VISIBLE
    }

    private fun setErrorScreen() {
        duckImageView.setImageResource(R.drawable.fail)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
