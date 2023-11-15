package com.garden.common

import com.google.android.gms.maps.model.LatLng

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

    const val ITEMS_PER_PAGE = 30

    /**
     * Prefetch distance which defines how far from the edge of loaded content
     * an access must be to trigger further loading.
     */
    const val ITEMS_PREFETCH_DISTANCE = 10

    /**
     *  Used when information about Water required by plants is missing from API
     *  Note: Value is in days
     */
    // TODO: Create inline class for water days
    const val DEFAULT_WATERING_NEED = 7

    const val MAX_GROWING_ZONES = 20

    /**
     * Wait for below time before starting hitting api.
     * This avoid hitting api multiple time while user types search query.
     */
    const val SEARCH_DEBOUNCE_TIME_MS: Long = 600

    /**
     * Dummy location to show on map
     */
    val MISSISSAUGA = LatLng(43.58, -79.64)

    /**
     * Dummy support number
     */
    const val SUPPORT_PHONE_NUMBER = "18662223456"
}
