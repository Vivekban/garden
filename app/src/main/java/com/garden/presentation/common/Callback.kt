package com.garden.presentation.common

/**
 * Type definition for Empty callback - shorthand for () -> Unit
 */
typealias VoidCallback = () -> Unit

/**
 * Type definition for Value/Data callback - shorthand for (value) -> Unit
 */
typealias ValueCallback<T> = (T) -> Unit
