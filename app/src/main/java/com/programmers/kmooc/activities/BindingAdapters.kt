package com.programmers.kmooc.activities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.network.ImageLoader
import com.programmers.kmooc.views.LectureDescriptionView

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("image_url")
    fun setImageUrl(view: ImageView, url: String) {
        ImageLoader.loadImage(url) { bitmap ->
            view.setImageBitmap(bitmap)
        }
    }

}