package com.garden.common

object Constant {
    const val BASE_URL = "https://perenual.com/api/"

    const val BASE_URL_UNSPLASH = "https://api.unsplash.com/"

    /**
     * If true database will be prefilled with sample plants
     */
    const val LOAD_DEFAULT_PLANT_IN_DB = false

    const val DATABASE_NAME = "garden-db"

    const val PLANT_DATA_FILENAME = "plants.json"

    const val PAGE_STARTING_INDEX = 1

    const val ITEM_PER_PAGE = 30

    /**
     *  Used when information about Water required by plants is missing from API
     *  Note: Value is in days
     */
    //TODO: Create inline class for water days
    const val DEFAULT_WATERING_NEED = 7

    const val MAX_GROWING_ZONES = 20

}