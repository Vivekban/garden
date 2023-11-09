package com.garden.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents a photo search response from Unsplash.
 */
data class UnsplashSearchDto(
    @field:SerializedName("results") val results: List<UnsplashPhotoDto>,
    @field:SerializedName("total_pages") val totalPages: Int
)
