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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.garden.R
import com.garden.domain.model.Plant


//@Composable
//fun PlantListScreen(
//    onPlantClick: (Plant) -> Unit,
//    modifier: Modifier = Modifier,
//    viewModel: PlantListViewModel = hiltViewModel(),
//) {
//    val plants by viewModel.plants.observeAsState(initial = emptyList())
//    PlantListScreen(plants = plants, modifier, onPlantClick = onPlantClick)
//}

@Composable
fun PlantListScreen(
    modifier: Modifier = Modifier,
    plants: LazyPagingItems<Plant>,
    searchQuery: String?,
    onPlantClick: (Plant) -> Unit = {},
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = plants.loadState) {
        if (plants.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                (plants.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.testTag("plant_list"),
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
                CircularProgressIndicator(Modifier.padding(dimensionResource(id = R.dimen.card_side_margin)))
            }
        }
    }

    if (plants.loadState.refresh is LoadState.Loading) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else if (plants.itemSnapshotList.isEmpty() &&
        (plants.loadState.refresh is LoadState.NotLoading || plants.loadState.append is LoadState.NotLoading)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Text(
                if ((searchQuery ?: "").isNotEmpty()) {
                    stringResource(R.string.no_plant_found_containing, searchQuery!!)
                } else stringResource(R.string.no_plant_found),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

}

//@Preview
//@Composable
//private fun PlantListScreenPreview(
//    @PreviewParameter(PlantListPreviewParamProvider::class) plants: List<Plant>
//) {
//    PlantListScreen(plants = plants)
//}

private class PlantListPreviewParamProvider : PreviewParameterProvider<List<Plant>> {
    override val values: Sequence<List<Plant>> =
        sequenceOf(
            emptyList(),
            listOf(
                Plant(1, "Apple", "Apple", growZoneNumber = 1),
                Plant(2, "Banana", "Banana", growZoneNumber = 2),
                Plant(3, "Carrot", "Carrot", growZoneNumber = 3),
                Plant(4, "Dill", "Dill", growZoneNumber = 3),
            )
        )
}