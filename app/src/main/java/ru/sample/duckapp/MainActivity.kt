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
    private lateinit var nextButton: Button
    private lateinit var duckImageView: ImageView
    private lateinit var inputField: EditText
    private lateinit var progressBar: ProgressBar

    private val ducksApi = Api.ducksApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        duckImageView = findViewById(R.id.duckImageView)
        nextButton = findViewById(R.id.nextButton)
        inputField = findViewById(R.id.inputField);
        progressBar = findViewById(R.id.loadingProgressBar)
        nextButton.setOnClickListener {
            fetchRandomDuckImage()
        }
    }

    private fun fetchRandomDuckImage() {
        val inputText = inputField.text?.toString() ?: ""
        progressBar.visibility = View.VISIBLE
        duckImageView.visibility = View.GONE
        if (inputText.isEmpty()) {
            getRandomDuckAsync()
        } else {
            val code = inputText.toIntOrNull()
            if (code != null && isHttpStatusCode(code)) {
                getCodeDuck(code)
            } else {
                // Handle invalid input (non-numeric or out of range)
            }
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
                    showToast("Invalid input!")
                    progressBar.visibility = View.GONE
                    duckImageView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                showToast("Error while fetching random duck!")
                progressBar.visibility = View.GONE
                duckImageView.visibility = View.VISIBLE

            }
        })
    }

    private fun getCodeDuck(code: Int) {
        loadImage("https://random-d.uk/api/v2/$code")
    }

    private fun isHttpStatusCode(code: Int): Boolean {
        return code in 100..599
    }

    private fun loadImage(imageUrl: String) {
        Picasso.get().load(imageUrl).into(duckImageView, object : com.squareup.picasso.Callback {
            override fun onSuccess() {
                progressBar.visibility = View.GONE
                duckImageView.visibility = View.VISIBLE
            }

            override fun onError(e: Exception?) {
                showToast("Unavailable duck!")
                progressBar.visibility = View.GONE
                duckImageView.visibility = View.VISIBLE
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
