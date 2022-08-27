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
    fun loadImage(url: String, completed: (Bitmap?) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val bitmapJob = async { convertUrlToBitmap(url) }
            MainScope().launch(Dispatchers.Main) {
                completed(bitmapJob.await())
            }
        }
    }


    private fun convertUrlToBitmap(url: String): Bitmap? {
        try {
            val url = URL(url)
            val conn = url.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            val input = conn.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            return bitmap
        } catch (e: IOException) {
            Log.e("", "convertUrlToBitmap: $url")
        }
        return null

    }
}