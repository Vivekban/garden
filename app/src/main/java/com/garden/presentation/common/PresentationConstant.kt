package com.garden.presentation.common

import com.google.android.gms.maps.model.LatLng

object PresentationConstant {

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
