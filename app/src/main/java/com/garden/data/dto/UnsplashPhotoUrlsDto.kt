package com.garden.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents URLs available for a Unsplash photo.
 */
data class UnsplashPhotoUrlsDto(
    @field:SerializedName("small") val small: String
)
