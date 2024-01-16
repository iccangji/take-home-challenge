package com.takehomechallenge.iksan.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteCharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg data: FavoriteCharacterEntity)

    @Query("DELETE FROM favorite WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM favorite WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

    @Query("SELECT * from favorite ORDER BY inserted_at DESC")
    fun getFavoriteList(): LiveData<List<FavoriteCharacterEntity>>
}