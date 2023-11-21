package com.garden.presentation.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.garden.R
import com.garden.domain.plant.Plant
import com.garden.domain.plantandplanting.PlantAndPlantings
import com.garden.domain.planting.Planting
import com.garden.presentation.common.VoidCallback
import com.garden.presentation.home.ShowSnackBar
import com.garden.presentation.plantlist.LoadingView
import com.garden.presentation.theme.card
import com.garden.presentation.view.EmptyListView
import com.garden.presentation.view.NetworkImage
import com.google.accompanist.themeadapter.material.MdcTheme
import java.util.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * This is used to determine number of column that can be shown in given space
 */
val idealTitleWidth: Dp = 180.dp

@Composable
fun GardenScreen(
    gardenPlants: LazyPagingItems<PlantAndPlantings>,
    modifier: Modifier = Modifier,
    searchQuery: String? = null,
    onAddPlantClick: () -> Unit = {},
    onPlantClick: (PlantAndPlantings) -> Unit = {},
    onClearSearchClick: VoidCallback = {},
    showSnackBar: ShowSnackBar? = null
) {
    val unknownError = stringResource(R.string.something_went_wrong)
    LaunchedEffect(key1 = gardenPlants.loadState.refresh) {
        if (gardenPlants.loadState.refresh is LoadState.Error) {
            showSnackBar?.invoke(
                (gardenPlants.loadState.refresh as LoadState.Error).error.message ?: unknownError,
                null
            )
        }
    }
    var widthInDp by remember { mutableStateOf(IntSize.Zero.width.dp) }
    val density = LocalDensity.current

    Box(
        modifier
            .fillMaxSize()
            .onSizeChanged {
                widthInDp = (it.width / density.density).toInt().dp
            }
    ) {
        GardenList(
            gardenPlants = gardenPlants,
            onPlantClick = onPlantClick,
            availableWidth = widthInDp
        )

        if (gardenPlants.loadState.refresh is LoadState.Loading) {
            LoadingView(modifier = Modifier.align(Alignment.Center))
        } else if (gardenPlants.itemSnapshotList.isEmpty() &&
            (
                gardenPlants.loadState.refresh is LoadState.NotLoading &&
                    gardenPlants.loadState.append is LoadState.NotLoading
                )
        ) {
            if (searchQuery?.isNotEmpty() == true) {
                EmptyListView(
                    modifier = Modifier.fillMaxSize(),
                    text = stringResource(R.string.no_plant_found_containing, searchQuery),
                    action = stringResource(id = R.string.clear_search),
                    callback = onClearSearchClick
                )
            } else {
                EmptyGarden(
                    modifier = Modifier.fillMaxSize(),
                    onAddPlantClick = onAddPlantClick
                )
            }
        }
    }
}

@Composable
private fun EmptyGarden(onAddPlantClick: () -> Unit, modifier: Modifier = Modifier) {
    EmptyListView(
        modifier = modifier,
        text = stringResource(id = R.string.garden_empty),
        action = stringResource(id = R.string.add_plant),
        callback = onAddPlantClick
    )
}

@Composable
private fun GardenList(
    gardenPlants: LazyPagingItems<PlantAndPlantings>,
    availableWidth: Dp,
    onPlantClick: (PlantAndPlantings) -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = (availableWidth / idealTitleWidth).toInt().coerceAtLeast(1)

    // Call reportFullyDrawn when the garden list has been rendered
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier,
        state = gridState,
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.card_side_margin),
            vertical = dimensionResource(id = R.dimen.margin_normal)
        )
    ) {
        items(
            gardenPlants.itemCount
        ) { index ->
            gardenPlants[index]?.let {
                GardenListItem(plant = it, onPlantClick = onPlantClick)
            }
        }

        // Loader at the end of the list
        item(span = { GridItemSpan(2) }) {
            if (gardenPlants.loadState.append is LoadState.Loading) {
                LoadingView(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.card_side_margin))
                )
            }
        }
    }
}

@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
private fun GardenListItem(
    plant: PlantAndPlantings,
    onPlantClick: (PlantAndPlantings) -> Unit
) {
    val vm = PlantAndGardenPlantingsViewModel(plant)

    // Dimensions
    val cardSideMargin = dimensionResource(id = R.dimen.card_side_margin)
    val marginSmall = dimensionResource(id = R.dimen.margin_small)

    Card(
        onClick = { onPlantClick(plant) },
        modifier = Modifier.padding(
            start = cardSideMargin,
            end = cardSideMargin,
            bottom = dimensionResource(id = R.dimen.card_bottom_margin)
        ),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        shape = MaterialTheme.shapes.card
    ) {
        Column(Modifier.fillMaxWidth()) {
            NetworkImage(
                model = vm.imageUrl,
                contentDescription = plant.plant.description,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.plant_item_image_height)),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(marginSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Plant name
                Text(
                    text = vm.plantName,
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(top = marginSmall))

                // Planted date
                Text(
                    text = stringResource(id = R.string.plant_date_header),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = vm.plantDateString,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.padding(top = marginSmall))

                // Last Watered
                Text(
                    text = stringResource(id = R.string.watered_date_header),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = vm.waterDateString,
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    text = pluralStringResource(
                        id = R.plurals.watering_next,
                        count = vm.wateringInterval,
                        vm.wateringInterval
                    ),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Preview
@Composable
private fun GardenScreenPreview(
    @PreviewParameter(GardenScreenPreviewParamProvider::class) gardenPlants:
        Flow<PagingData<PlantAndPlantings>>
) {
    MdcTheme {
        GardenScreen(gardenPlants.collectAsLazyPagingItems())
    }
}

private class GardenScreenPreviewParamProvider :
    PreviewParameterProvider<Flow<PagingData<PlantAndPlantings>>> {
    override val values: Sequence<Flow<PagingData<PlantAndPlantings>>> =
        sequenceOf(
            flowOf(PagingData.from(listOf())),
            flowOf(
                PagingData.from(
                    listOf(
                        PlantAndPlantings(
                            plant = Plant(
                                id = 1,
                                name = "Apple",
                                description = "An apple.",
                                growZoneNumber = 1,
                                wateringInterval = 2,
                                imageUrl =
                                "https://images.unsplash.com/photo-1417325384643-" +
                                    "aac51acc9e5d?q=75&fm=jpg&w=400&fit=max"
                            ),
                            plantings = listOf(
                                Planting(
                                    plantId = 1,
                                    plantDate = Calendar.getInstance(),
                                    lastWateringDate = Calendar.getInstance()
                                )
                            )
                        )

                    )
                )
            )
        )
}
