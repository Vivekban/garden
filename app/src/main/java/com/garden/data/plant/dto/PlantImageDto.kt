package com.garden.data.plant.dto

import com.google.gson.annotations.SerializedName

data class PlantImageDto(
    @SerializedName("original_url")
    val originalUrl: String?,
    @SerializedName("regular_url")
    val smallUrl: String?
)
