package com.programmers.kmooc.activities.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.KmoocApplication
import com.programmers.kmooc.databinding.ActivityKmookDetailBinding
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.network.ImageLoader
import com.programmers.kmooc.utils.DateUtil
import com.programmers.kmooc.utils.toVisibility
import com.programmers.kmooc.viewmodels.KmoocDetailViewModel
import com.programmers.kmooc.viewmodels.KmoocDetailViewModelFactory

class KmoocDetailActivity : AppCompatActivity() {

    companion object {
        const val INTENT_PARAM_COURSE_ID = "param_course_id"
    }

    private lateinit var binding: ActivityKmookDetailBinding
    private lateinit var viewModel: KmoocDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKmookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.also { it ->
            setSupportActionBar(it)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val kmoocRepository = (application as KmoocApplication).kmoocRepository
        viewModel = ViewModelProvider(this, KmoocDetailViewModelFactory(kmoocRepository)).get(
            KmoocDetailViewModel::class.java
        )
        val lectureId: String = intent.extras!!.getString(INTENT_PARAM_COURSE_ID)!!
        viewModel.detail(lectureId)
        viewModel.lecture.observe(this) {
            binding.apply {

                ImageLoader.loadImage(it.courseImageLarge) { it ->
//                    lectureImage.setImageBitmap(it)
                    lectureImage.setImageBitmap(it)
                }

                lectureNumber.setDescription(title = "강좌번호", description = it.number)
                lectureType.setDescription(title = "강좌분류", description = it.classfyName)
                lectureOrg.setDescription(title = "운영기관", description = it.orgName)
                lectureTeachers.setDescription(title = "교수정보", description = it.teachers ?: "")
                lectureDue.setDescription(
                    title = "강좌기관",
                    description = DateUtil.formatDueString(it.start, it.end)
                )
                webView.settings.javaScriptEnabled = true
                webView.loadData(it.overview, "text/html; charset=utf-8", "UTF-8")
            }
        }
        viewModel.processing.observe(this) {
            binding.progressBar.isVisible = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}