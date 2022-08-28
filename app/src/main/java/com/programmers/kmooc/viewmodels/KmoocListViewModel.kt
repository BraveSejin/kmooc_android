package com.programmers.kmooc.viewmodels

import androidx.lifecycle.*
import com.programmers.kmooc.models.LectureList
import com.programmers.kmooc.repositories.KmoocRepository

class KmoocListViewModel(private val repository: KmoocRepository) : ViewModel() {

    private val _lectureList = MutableLiveData<LectureList>()
    val lectureList: LiveData<LectureList> = _lectureList

    private val _processing = MutableLiveData<Boolean>()
    val processing: LiveData<Boolean> = _processing

    fun list() {
        _processing.value = true
        repository.list { lectureList ->
            _lectureList.postValue(lectureList)
            _processing.postValue(false)
        }

    }

    fun hasNext(): Boolean = !lectureList.value?.lectures.isNullOrEmpty()

    fun next() {

        _processing.value = true
        val curLectureList = this._lectureList.value ?: return
        repository.next(curLectureList) { lectureList ->
            val curLectures = curLectureList.lectures
            val newLectures = curLectures + lectureList.lectures
            lectureList.lectures = newLectures
            _lectureList.postValue(lectureList)
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