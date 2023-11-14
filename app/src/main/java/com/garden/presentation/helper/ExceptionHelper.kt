package com.garden.presentation.helper

import java.net.UnknownHostException

fun Throwable.toUserFriendlyMessage(): String {

    return when (this) {
        is UnknownHostException -> {
            "Network Unavailable! Please check internet settings"
        }

        else -> {
            message ?: "Something went wrong"
        }
    }

}