package com.takehomechallenge.iksan.utils

import com.takehomechallenge.iksan.data.database.FavoriteCharacterEntity

object DummyData {
    fun generateFavoriteList(): List<FavoriteCharacterEntity> {
        val charList = ArrayList<FavoriteCharacterEntity>()
        for (i in 0..10) {
            val char = FavoriteCharacterEntity(
                image = "https://png.pngtree.com/png-vector/20191101/ourmid/pngtree-cartoon-color-simple-male-avatar-png-image_1934459.jpg",
                name = "Character $i",
                id = i,
                insertedAt = 0
            )
            charList.add(char)
        }
        return charList
    }
}