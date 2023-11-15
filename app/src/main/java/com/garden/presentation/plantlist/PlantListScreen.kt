package com.garden.presentation.plantlist

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.garden.R
import com.garden.common.ValueCallback
import com.garden.common.VoidCallback
import com.garden.domain.model.Plant
import com.garden.presentation.helper.toUserFriendlyMessage
import com.garden.presentation.view.EmptyListView

/**
 * This is used to determine number of column that can be shown in given space
 */
val idealTitleWidth: Dp = 180.dp

@Composable
fun PlantListScreen(
    plants: LazyPagingItems<Plant>,
    searchQuery: String?,
    modifier: Modifier = Modifier,
    onPlantClick: ValueCallback<Plant> = {},
    onClearSearchClick: VoidCallback = {}
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = plants.loadState) {
        if (plants.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                (plants.loadState.refresh as LoadState.Error).error.toUserFriendlyMessage(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    var widthInDp by remember { mutableStateOf(IntSize.Zero.width.dp) }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                widthInDp = (it.width / density.density).toInt().dp
            }
    ) {
        if (plants.itemSnapshotList.isNotEmpty()) {
            PlantList(plants, widthInDp, onPlantClick)
        }

        if (plants.loadState.refresh is LoadState.Loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                LoadingView(modifier = Modifier.align(Alignment.Center))
            }
        } else if ((plants.loadState.append is LoadState.Loading).not()) {
            // since there is no loading show no items available message
            if (plants.itemSnapshotList.isEmpty()) {
                val hasSearchQuery = searchQuery?.isNotEmpty() == true

                val text = when {
                    plants.loadState.refresh is LoadState.Error ->
                        (plants.loadState.refresh as LoadState.Error).error.toUserFriendlyMessage()

                    hasSearchQuery -> stringResource(
                        R.string.no_plant_found_containing,
                        searchQuery!!
                    )

                    else -> stringResource(R.string.no_plant_found)
                }

                EmptyListView(
                    modifier = Modifier.fillMaxSize(),
                    text = text,
                    action = if (hasSearchQuery) {
                        stringResource(id = R.string.clear_search)
                    } else {
                        null
                    },
                    callback = onClearSearchClick
                )
            }
        }
    }
}

@Composable
private fun PlantList(
    plants: LazyPagingItems<Plant>,
    availableWidth: Dp,
    onPlantClick: ValueCallback<Plant>,
    modifier: Modifier = Modifier
) {
    val columns = (availableWidth / idealTitleWidth).toInt().coerceAtLeast(1)

    LazyVerticalGrid(
        modifier = modifier.testTag("plant_list"),
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.card_side_margin),
            vertical = dimensionResource(id = R.dimen.header_margin)
        )
    ) {
        items(
            plants.itemCount
        ) { index ->
            plants[index]?.let { plant ->
                PlantListItem(plant = plant) {
                    onPlantClick(plant)
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            if (plants.loadState.append is LoadState.Loading) {
                LoadingView(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.card_side_margin))
                )
            }
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) =
    CircularProgressIndicator(modifier.testTag("loader"), color = MaterialTheme.colors.onBackground)

// @Preview
// @Composable
// private fun PlantListScreenPreview(
//    @PreviewParameter(PlantListPreviewParamProvider::class) plants: List<Plant>
// ) {
//    PlantListScreen(plants = plants)
// }

private class PlantListPreviewParamProvider : PreviewParameterProvider<List<Plant>> {
    override val values: Sequence<List<Plant>> =
        sequenceOf(
            emptyList(),
            listOf(
                Plant(1, "Apple", "Apple", growZoneNumber = 1),
                Plant(2, "Banana", "Banana", growZoneNumber = 2),
                Plant(3, "Carrot", "Carrot", growZoneNumber = 3),
                Plant(4, "Dill", "Dill", growZoneNumber = 3)
            )
        )
}
