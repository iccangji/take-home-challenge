package com.takehomechallenge.iksan.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteCharacterEntity::class],
    version = 1,
    exportSchema = false)
abstract class FavoriteCharacterDatabase : RoomDatabase() {
    abstract fun favoriteCharDao(): FavoriteCharacterDao

    companion object {
        @Volatile
        private var instance: FavoriteCharacterDatabase? = null
        fun getInstance(context: Context): FavoriteCharacterDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteCharacterDatabase::class.java, "favorite_character.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}