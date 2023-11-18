package com.garden.presentation.helper

import com.garden.R
import java.net.UnknownHostException

fun Throwable.toUserFriendlyMessage(): Int {
    return when (this) {
        is UnknownHostException -> R.string.network_unavailable

        else -> R.string.something_went_wrong
    }
}
