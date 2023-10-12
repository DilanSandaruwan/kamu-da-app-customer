package com.dilan.kamuda.customerapp.viewmodels.foodhouse

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dilan.kamuda.customerapp.model.foodhouse.FoodMenu
import com.dilan.kamuda.customerapp.network.utils.ApiState
import com.dilan.kamuda.customerapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodHouseViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application,
) : AndroidViewModel(application) {

    private val _menuList = MutableLiveData<List<FoodMenu>>()
    val menuList: LiveData<List<FoodMenu>>
        get() = _menuList

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    fun getMenuListForAll() {
        _showLoader.value = true
        viewModelScope.launch {

            when (val res = mainRepository.getMenuListForMealFromDataSource()) {
                is ApiState.Success -> {
                    _showLoader.postValue(false)
                    Log.e("Doggy", "getMenuListForAll: ${res.data.get(0)}")
                    _menuList.postValue(res.data ?: emptyList())
                }

                is ApiState.Failure -> {
                    _showLoader.postValue(false)
                    Log.e("Doggy", "getMenuListForAll: ${res.msg}")
                    _menuList.postValue(emptyList())
                }

                is ApiState.Loading -> {
                    _showLoader.postValue(true)
                    Log.e("Doggy", "getMenuListForAll: ${res.toString()}")
                }
            }

        }
    }

//    fun findTodaysLectureList() {
//        _lectureListResult.value = Result.Loading
//        viewModelScope.launch {
//            val result: Result<List<Lecture>> = lectureRepo.findTodaysLectureList()
//            _lectureListResult.value = result
//            when (result) {
//                is Result.Success<*> -> {
//                    lectureListUiState.loadLectureList((result as Result.Success<List<Lecture>>).data)
//                }
//                else -> {lectureListUiState.loadLectureList(emptyList())}
//            }
//        }
//    }

    init {
        getMenuListForAll()
    }
}