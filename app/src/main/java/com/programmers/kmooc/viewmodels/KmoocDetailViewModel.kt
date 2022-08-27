package com.programmers.kmooc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.repositories.KmoocRepository


class KmoocDetailViewModel(private val repository: KmoocRepository) : ViewModel() {
    private val _lecture = MutableLiveData<Lecture>()
    val lecture: LiveData<Lecture> = _lecture

    private val _processing = MutableLiveData<Boolean>()
    val processing: LiveData<Boolean> = _processing

    fun detail(courseId: String) {
        _processing.value = true
        repository.detail(courseId) { lecture ->
            _lecture.postValue(lecture)
            _processing.postValue(false)
        }
    }
}

class KmoocDetailViewModelFactory(private val repository: KmoocRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KmoocDetailViewModel::class.java)) {
            return KmoocDetailViewModel(repository) as T
        }
        throw IllegalAccessException("Unkown Viewmodel Class")
    }
}