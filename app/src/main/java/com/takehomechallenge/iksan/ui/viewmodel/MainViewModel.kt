package com.takehomechallenge.iksan.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.takehomechallenge.iksan.data.injection.Injection
import com.takehomechallenge.iksan.data.model.CharacterItem
import com.takehomechallenge.iksan.data.repository.CharacterRepository

class MainViewModel (repository: CharacterRepository) : ViewModel() {

    val getCharacterList: LiveData<PagingData<CharacterItem>> = repository.getCharacterList().cachedIn(viewModelScope)
}

class MainViewModelFactory private constructor(
    private val repository: CharacterRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("No ModelClass: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null
        fun getInstance(context: Context): MainViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(Injection.getRepository(context))
            }.also { instance = it }
        }
    }
}