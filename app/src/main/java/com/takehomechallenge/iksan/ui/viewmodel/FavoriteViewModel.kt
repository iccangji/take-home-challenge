package com.takehomechallenge.iksan.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.takehomechallenge.iksan.data.database.FavoriteCharacterEntity
import com.takehomechallenge.iksan.data.injection.Injection
import com.takehomechallenge.iksan.data.repository.CharacterRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: CharacterRepository) : ViewModel() {
    fun getFavoriteList() = repository.getFavoriteList()
    fun deleteFavorite(char: FavoriteCharacterEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteFavorite(char.id)
        }
        getFavoriteList()
    }
}

class FavoriteViewModelFactory private constructor(
    private val repository: CharacterRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("No ModelClass: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteViewModelFactory? = null
        fun getInstance(context: Context): FavoriteViewModelFactory {
            return instance ?: synchronized(this) {
                FavoriteViewModelFactory(Injection.getRepository(context))
            }.also { instance = it }
        }
    }
}