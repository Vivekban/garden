package com.garden.presentation.garden

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.garden.R
import com.garden.domain.model.Plant
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import com.garden.presentation.viewmodels.PlantAndGardenPlantingsViewModel
import com.garden.ui.theme.card
import com.garden.ui.utils.GardenImage
import com.google.accompanist.themeadapter.material.MdcTheme
import java.util.*

@Composable
fun GardenScreen(
    gardenPlants: List<PlantAndPlantings>,
    modifier: Modifier = Modifier,
    onAddPlantClick: () -> Unit = {},
    onPlantClick: (PlantAndPlantings) -> Unit = {}
) {
    if (gardenPlants.isEmpty()) {
        EmptyGarden(onAddPlantClick, modifier)
    } else {
        GardenList(gardenPlants = gardenPlants, onPlantClick = onPlantClick, modifier = modifier)
    }
}

@Composable
private fun GardenList(
    gardenPlants: List<PlantAndPlantings>,
    onPlantClick: (PlantAndPlantings) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Call reportFullyDrawn when the garden list has been rendered
    val gridState = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier,
        state = gridState,
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.card_side_margin),
            vertical = dimensionResource(id = R.dimen.margin_normal)
        )
    ) {
        items(
            items = gardenPlants,
            key = { it.plant.id }
        ) {
            GardenListItem(plant = it, onPlantClick = onPlantClick)
        }
    }
}

@OptIn(
    ExperimentalMaterialApi::class,
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
        shape = MaterialTheme.shapes.card,
    ) {
        Column(Modifier.fillMaxWidth()) {
            GardenImage(
                model = vm.imageUrl,
                contentDescription = plant.plant.description,
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.plant_item_image_height)),
                contentScale = ContentScale.Crop,
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

@Composable
private fun EmptyGarden(onAddPlantClick: () -> Unit, modifier: Modifier = Modifier) {
    // Calls reportFullyDrawn when this composable is composed.
    ReportDrawn()

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.garden_empty),
            style = MaterialTheme.typography.h5
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = dimensionResource(id = R.dimen.button_corner_radius),
                bottomStart = dimensionResource(id = R.dimen.button_corner_radius),
                bottomEnd = 0.dp,
            ),
            onClick = onAddPlantClick
        ) {
            Text(
                color = MaterialTheme.colors.primary,
                text = stringResource(id = R.string.add_plant)
            )
        }
    }
}

@Preview
@Composable
private fun GardenScreenPreview(
    @PreviewParameter(GardenScreenPreviewParamProvider::class) gardenPlants: List<PlantAndPlantings>
) {
    MdcTheme {
        GardenScreen(gardenPlants)
    }
}

private class GardenScreenPreviewParamProvider :
    PreviewParameterProvider<List<PlantAndPlantings>> {
    override val values: Sequence<List<PlantAndPlantings>> =
        sequenceOf(
            emptyList(),
            listOf(
                PlantAndPlantings(
                    plant = Plant(
                        id = 1,
                        name = "Apple",
                        description = "An apple.",
                        growZoneNumber = 1,
                        wateringInterval = 2,
                        imageUrl = "https://images.unsplash.com/photo-1417325384643-aac51acc9e5d?q=75&fm=jpg&w=400&fit=max",
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
}