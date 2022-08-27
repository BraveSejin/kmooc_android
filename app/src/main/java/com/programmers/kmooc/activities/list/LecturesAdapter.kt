package com.programmers.kmooc.activities.list

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.programmers.kmooc.R
import com.programmers.kmooc.databinding.ViewKmookListItemBinding
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.network.ImageLoader
import com.programmers.kmooc.utils.DateUtil
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class LecturesAdapter : RecyclerView.Adapter<LectureViewHolder>() {

    private val lectures = mutableListOf<Lecture>()
    var onClick: (Lecture) -> Unit = {}

    fun updateLectures(lectures: List<Lecture>) {
        val prevvSize = lectures.size
        this.lectures.clear()
        this.lectures.addAll(lectures)
//        notifyDataSetChanged()
        notifyItemRangeInserted(prevvSize,10)
    }

    override fun getItemCount(): Int {
        return lectures.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_kmook_list_item, parent, false)
        val binding = ViewKmookListItemBinding.bind(view)
        return LectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.itemView.setOnClickListener { onClick(lecture) }
        holder.bind(lecture)
    }
}

class LectureViewHolder(private val binding: ViewKmookListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(lecture: Lecture) {
        binding.apply {
            ImageLoader.loadImage(lecture.courseImage, { bitmap ->
                lectureImage.setImageBitmap(bitmap)
            })
            lectureTitle.text = lecture.name
            lectureFrom.text = lecture.orgName
            lectureDuration.text =
                DateUtil.formatDate(lecture.start) + "~" + DateUtil.formatDate(lecture.end)

        }
    }


}