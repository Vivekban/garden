package com.garden.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents a photo from Unsplash.
 *
 * [here](https://unsplash.com/documentation#get-a-photo).
 */
data class UnsplashPhotoDto(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("urls") val urls: UnsplashPhotoUrlsDto,
    @field:SerializedName("user") val user: UnsplashUserDto
)
