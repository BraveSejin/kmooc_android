package com.programmers.kmooc.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

object ImageLoader {
    private val cache = mutableMapOf<String, Bitmap>()

    fun loadImage(url: String, completed: (Bitmap?) -> Unit) {
        if (cache.containsKey(url)) {
            completed(cache[url])
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                cache[url] = bitmap
                launch(Dispatchers.Main) {
                    completed(bitmap)
                }

            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    completed(null)
                }
            }
        }
    }
}