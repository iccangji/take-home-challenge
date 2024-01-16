package com.takehomechallenge.iksan.data.injection

import android.content.Context
import com.takehomechallenge.iksan.data.database.FavoriteCharacterDatabase
import com.takehomechallenge.iksan.data.repository.CharacterRepository
import com.takehomechallenge.iksan.data.retrofit.ApiConfig

object Injection {
    fun getRepository(context: Context): CharacterRepository{
        val apiService = ApiConfig.getApiService()
        val favCharDatabase = FavoriteCharacterDatabase.getInstance(context)
        return CharacterRepository.getInstance(
            apiService = apiService,
            dao = favCharDatabase.favoriteCharDao()
        )
    }
}