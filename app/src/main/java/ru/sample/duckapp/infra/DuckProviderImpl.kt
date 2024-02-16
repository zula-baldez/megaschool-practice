//package ru.sample.duckapp.infra
//
//import android.widget.ImageView
//import com.squareup.picasso.Picasso
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.suspendCancellableCoroutine
//import kotlinx.coroutines.withContext
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import ru.sample.duckapp.domain.Duck
//import kotlin.coroutines.resume
//import kotlin.coroutines.resumeWithException
//
//class DuckProviderImpl : DuckProvider {
//
//    override fun fetchRandomDuckImage() : ImageView? {
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val duck = withContext(Dispatchers.IO) { getRandomDuckAsync() }
//                duck?.let {
//                    loadImage(it.url)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private suspend fun getRandomDuckAsync(): Duck? {
//        return suspendCancellableCoroutine { continuation ->
//            ducksApi.getRandomDuck().enqueue(object : Callback<Duck> {
//                override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
//                    if (response.isSuccessful) {
//                        val duck = response.body()
//                        continuation.resume(duck)
//                    } else {
//                        continuation.resumeWithException(Exception("Failed to get duck"))
//                    }
//                }
//
//                override fun onFailure(call: Call<Duck>, t: Throwable) {
//                    continuation.resumeWithException(t)
//                }
//            })
//
//            continuation.invokeOnCancellation {
//                it?.printStackTrace()
//            }
//        }
//    }
//
//    private suspend fun loadImage(imageUrl: String) : ImageView? {
//        // Load the image using Picasso in the main thread
//        withContext(Dispatchers.Main) {
//            Picasso.get().load(imageUrl).into(duckImageView)
//        }
//    }
//
//
//}