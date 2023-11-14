package com.garden.data.dto


import com.garden.common.Constant
import com.garden.fake.model.testPlantDescription
import com.google.gson.annotations.SerializedName

/**
 * Plant data object that comes from network
 */
data class PlantDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("common_name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("cycle")
    val cycle: String?,

    @SerializedName("default_image")
    val plantImageDto: PlantImageDto?,

    @SerializedName("watering")
    val watering: WateringNeed?
) {

    fun generateDescription(): String {
        //TODO: testPlantDescription
        return description ?: testPlantDescription
    }

    fun growZoneNumber() = id % Constant.MAX_GROWING_ZONES

    fun wateringInDays() = watering?.inDays() ?: Constant.DEFAULT_WATERING_NEED

}


enum class WateringNeed {

    @SerializedName("none")
    None,

    @SerializedName("minimum")
    Minimum,

    @SerializedName("average")
    Average,

    @SerializedName("frequent")
    Frequent;

    fun inDays(): Int {
        return when (this) {
            None -> 30
            Minimum -> 15
            Average -> 7
            Frequent -> 1
        }
    }

}
