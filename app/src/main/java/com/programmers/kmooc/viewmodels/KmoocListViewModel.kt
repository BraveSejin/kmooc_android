package com.programmers.kmooc.viewmodels

import androidx.lifecycle.*
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.models.LectureList
import com.programmers.kmooc.repositories.KmoocRepository

class KmoocListViewModel(private val repository: KmoocRepository) : ViewModel() {

    private val _lectures = MutableLiveData<List<Lecture>>()
    val lectures: LiveData<List<Lecture>> = _lectures

    private val _processing = MutableLiveData<Boolean>()
    val processing: LiveData<Boolean> = _processing

    private var currentLectureList = LectureList.EMPTY

    fun list() {
        _processing.value = true
        repository.list { lectureList ->
            currentLectureList = lectureList
            _lectures.postValue(lectureList.lectures)
            _processing.postValue(false)
        }

    }

    fun next() {
        _processing.value = true
        repository.next(currentLectureList) { lectureList ->
            val cur = _lectures.value
            val next = cur!! + lectureList.lectures
            currentLectureList = lectureList
            _lectures.postValue(next)
            _processing.postValue(false)
        }
    }
}

class KmoocListViewModelFactory(private val repository: KmoocRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KmoocListViewModel::class.java)) {
            return KmoocListViewModel(repository) as T
        }
        throw IllegalAccessException("Unkown Viewmodel Class")
    }
}