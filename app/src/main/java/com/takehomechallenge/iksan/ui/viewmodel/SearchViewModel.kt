package com.takehomechallenge.iksan.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.takehomechallenge.iksan.data.injection.Injection
import com.takehomechallenge.iksan.data.model.CharacterListResponse
import com.takehomechallenge.iksan.data.repository.CharacterRepository
import com.takehomechallenge.iksan.data.repository.UiState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val repository: CharacterRepository) : ViewModel() {
    private val _searchEmptyState = MutableLiveData<UiState<String>>()
    val searchEmptyState: LiveData<UiState<String>> = _searchEmptyState
    fun resultIsEmpty(query: String){
        _searchEmptyState.value = UiState.Loading
        val searchCharacterCall: Call<CharacterListResponse> = repository.searchCharacterCall(query)
        searchCharacterCall.enqueue(object : Callback<CharacterListResponse> {
            override fun onResponse(
                call: Call<CharacterListResponse>,
                response: Response<CharacterListResponse>
            ) {
                if(response.isSuccessful){
                    val responseData : CharacterListResponse? = response.body()
                    if (responseData != null) {
                        _searchEmptyState.value =  UiState.Success(query)
                    }
                } else{
                    val errorBody = response.errorBody()?.charStream()?.readText() ?: ""
                    return if(errorBody == EMPTY_MESSAGE){
                        _searchEmptyState.value =  UiState.Empty
                    }else{
                        _searchEmptyState.value = UiState.Error
                    }
                }
            }

            override fun onFailure(call: Call<CharacterListResponse>, t: Throwable) {
                _searchEmptyState.value = UiState.Error
            }
        })
    }

    fun searchResult(query: String) = repository.searchCharacter(query).cachedIn(viewModelScope)



    companion object{
        const val EMPTY_MESSAGE = "{\"error\":\"There is nothing here\"}"
    }
}

class SearchViewModelFactory private constructor(
    private val repository: CharacterRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("No ModelClass: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SearchViewModelFactory? = null
        fun getInstance(context: Context): SearchViewModelFactory {
            return instance ?: synchronized(this) {
                SearchViewModelFactory(Injection.getRepository(context))
            }.also { instance = it }
        }
    }
}