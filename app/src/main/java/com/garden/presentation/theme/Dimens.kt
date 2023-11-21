package com.garden.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.garden.R

/**
 * Class that captures dimens used in Compose code. The dimens that need to be consistent with the
 * View system use [dimensionResource] and are marked as composable.
 *
 */
object Dimens {

    val PaddingExtraSmall: Dp
        @Composable get() = dimensionResource(R.dimen.margin_extra_small)

    val PaddingSmall: Dp
        @Composable get() = dimensionResource(R.dimen.margin_small)

    val PaddingNormal: Dp
        @Composable get() = dimensionResource(R.dimen.margin_normal)

    val PaddingLarge: Dp = 24.dp

    val PlantDetailAppBarHeight: Dp
        @Composable get() = dimensionResource(R.dimen.plant_detail_app_bar_height)

    val ToolbarIconPadding = 12.dp

    val ToolbarIconSize = 32.dp
}

object AppMargin {
    val cardSide: Dp
        @Composable get() = dimensionResource(R.dimen.card_side_margin)

    val cardBottom: Dp
        @Composable get() = dimensionResource(R.dimen.card_bottom_margin)
}
