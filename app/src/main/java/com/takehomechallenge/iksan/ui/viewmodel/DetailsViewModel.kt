package com.takehomechallenge.iksan.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.iksan.data.database.FavoriteCharacterEntity
import com.takehomechallenge.iksan.data.injection.Injection
import com.takehomechallenge.iksan.data.model.CharacterDetails
import com.takehomechallenge.iksan.data.repository.CharacterRepository
import com.takehomechallenge.iksan.data.repository.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: CharacterRepository) : ViewModel() {
    private val _uiState = MutableLiveData<UiState<CharacterDetails>>()
    val uiState: LiveData<UiState<CharacterDetails>> = _uiState
    fun getCharacterDetails(id: Int){
        viewModelScope.launch {
            try {
                val char = repository.getCharacterDetails(id)
                _uiState.value = UiState.Success(char)
            } catch (e: Exception) {
                _uiState.value = UiState.Error
            }
        }
    }

    fun deleteFavorite(idChar: Int){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteFavorite(idChar)
        }
        getCharacterDetails(idChar)
    }

    fun addFavorite(char: CharacterDetails, timestamp: Long){
        val data = FavoriteCharacterEntity(
            id = char.id,
            name = char.name,
            image = char.image,
            insertedAt = timestamp,
        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertFavorite(data)
        }

        getCharacterDetails(char.id)
    }
}

class DetailsViewModelFactory private constructor(
    private val repository: CharacterRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("No ModelClass: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailsViewModelFactory? = null
        fun getInstance(context: Context): DetailsViewModelFactory {
            return instance ?: synchronized(this) {
                DetailsViewModelFactory(Injection.getRepository(context))
            }.also { instance = it }
        }
    }
}