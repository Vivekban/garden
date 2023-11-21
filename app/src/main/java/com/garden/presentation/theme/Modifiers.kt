package com.garden.presentation.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints

/**
 * Hides an element on the screen leaving its space occupied.
 * This should be replaced with the real visible modifier in the future:
 * https://issuetracker.google.com/issues/158837937
 *
 * isVisible is of type () -> Boolean because if the calling composable doesn't own the
 * state boolean of that Boolean, a read (recompose) will be avoided.
 */
fun Modifier.visible(isVisible: () -> Boolean) = this.then(VisibleModifier(isVisible))

private data class VisibleModifier(
    private val isVisible: () -> Boolean
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            if (isVisible()) {
                placeable.place(0, 0)
            }
        }
    }
}
