package com.takehomechallenge.iksan.data.model

import com.google.gson.annotations.SerializedName

data class CharacterListResponse(

	@field:SerializedName("results")
	val results: List<CharacterItem>? = null,

	@field:SerializedName("info")
	val info: Info? = null,
)

data class Location(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("url")
	val url: String
)

data class Info(

	@field:SerializedName("next")
	val next: String,

	@field:SerializedName("pages")
	val pages: Int,

	@field:SerializedName("prev")
	val prev: Any,

	@field:SerializedName("count")
	val count: Int
)

data class CharacterItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("species")
	val species: String,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("origin")
	val origin: Origin,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: Location,

	@field:SerializedName("episode")
	val episode: List<String>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("status")
	val status: String
)

data class Origin(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("url")
	val url: String
)

data class CharacterDetails(
    val image: String,
    val id: Int,
    val gender: String,
    val species: String,
    val origin: Origin,
    val name: String,
    val location: Location,
    var isFavorite: Boolean,
)
