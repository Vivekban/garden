package com.garden.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents a user from Unsplash.
 *
 * [here](https://unsplash.com/documentation#get-a-users-public-profile).
 */
data class UnsplashUserDto(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("username") val username: String
) {
    val attributionUrl: String
        get() {
            return "https://unsplash.com/$username?utm_source=sunflower&utm_medium=referral"
        }
}
