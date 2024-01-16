package com.takehomechallenge.iksan.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.takehomechallenge.iksan.data.database.FavoriteCharacterDao
import com.takehomechallenge.iksan.data.database.FavoriteCharacterEntity
import com.takehomechallenge.iksan.data.model.CharacterDetails
import com.takehomechallenge.iksan.data.model.CharacterItem
import com.takehomechallenge.iksan.data.model.CharacterListResponse
import com.takehomechallenge.iksan.data.paging.CharacterListPagingSource
import com.takehomechallenge.iksan.data.paging.SearchCharacterPagingSource
import com.takehomechallenge.iksan.data.retrofit.ApiService
import retrofit2.Call

interface AppDataRepository{
    fun getCharacterList() : LiveData<PagingData<CharacterItem>>
    fun searchCharacter(query: String) : LiveData<PagingData<CharacterItem>>
    fun searchCharacterCall(query: String): Call<CharacterListResponse>
    suspend fun getCharacterDetails(id: Int) : CharacterDetails
    fun getFavoriteList(): LiveData<UiState<List<FavoriteCharacterEntity>>>
    suspend fun insertFavorite(char: FavoriteCharacterEntity)
    suspend fun deleteFavorite(idChar: Int)
}

class CharacterRepository private constructor(
    private val apiService: ApiService,
    private val dao: FavoriteCharacterDao
) : AppDataRepository {

    override fun getCharacterList() : LiveData<PagingData<CharacterItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6
            ),
            pagingSourceFactory = {
                CharacterListPagingSource(apiService = apiService)
            }
        ).liveData
    }
    override fun searchCharacter(query: String) : LiveData<PagingData<CharacterItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 6
            ),
            pagingSourceFactory = {
                SearchCharacterPagingSource(
                    apiService = apiService,
                    query = query
                )
            }
        ).liveData
    }
    override fun searchCharacterCall(query: String): Call<CharacterListResponse> = apiService.searchIsEmpty(query)

    override suspend fun getCharacterDetails(id: Int): CharacterDetails {
        val result = apiService.getCharacterDetails(id)
        return CharacterDetails(
            id = id,
            name = result.name,
            species = result.species,
            gender = result.gender,
            origin = result.origin,
            location = result.location,
            image = result.image,
            isFavorite = dao.isFavorite(id),
        )
    }
    override fun getFavoriteList(): LiveData<UiState<List<FavoriteCharacterEntity>>> = liveData {
        emit(UiState.Loading)
        try {
            val data = dao.getFavoriteList()
            emitSource(data.map { UiState.Success(it) })
        } catch (e: Exception){
            emit(UiState.Error)
        }
    }
    override suspend fun insertFavorite(char: FavoriteCharacterEntity) {
        try {
            dao.insert(char)
        }catch (e: Exception){
            Log.d("E", e.toString())
        }
    }

    override suspend fun deleteFavorite(idChar: Int) {
        try {
            dao.delete(idChar)
        }catch (e: Exception){
            Log.d("E", e.toString())
        }
    }

    companion object {
        @Volatile
        private var instance: CharacterRepository? = null
        fun getInstance(
            apiService: ApiService,
            dao: FavoriteCharacterDao
        ): CharacterRepository =
            instance ?: synchronized(this) {
                instance ?: CharacterRepository(apiService, dao)
            }.also { instance = it }
    }
}

sealed class UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>()
    data object Error : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data object Empty : UiState<Nothing>()
}