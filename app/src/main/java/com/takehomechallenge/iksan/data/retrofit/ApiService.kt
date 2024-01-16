package com.takehomechallenge.iksan.data.retrofit

import com.takehomechallenge.iksan.data.model.CharacterItem
import com.takehomechallenge.iksan.data.model.CharacterListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("character/")
    suspend fun getCharacterList(
        @Query("page") id: Int
    ): CharacterListResponse
    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") id: Int
    ): CharacterItem
    @GET("character/")
    suspend fun searchCharacter(
        @Query("name") query: String,
        @Query("page") page: Int
    ): CharacterListResponse
    @GET("character/")
    fun searchIsEmpty(
        @Query("name") query: String,
        @Query("page") page: Int = 1
    ): Call<CharacterListResponse>

}