package com.garden.presentation.plantlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.garden.R
import com.garden.domain.plant.Plant
import com.garden.presentation.theme.card
import com.garden.presentation.view.NetworkImage

@Composable
fun PlantListItem(plant: Plant, onClick: () -> Unit) {
    ImageListItem(name = plant.name, imageUrl = plant.imageUrl, onClick = onClick)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageListItem(
    name: String,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        elevation = dimensionResource(id = R.dimen.card_elevation),
        shape = MaterialTheme.shapes.card,
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
    ) {
        Column(Modifier.fillMaxWidth()) {
            NetworkImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.a11y_plant_item_image),
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.plant_item_image_height)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.margin_normal))
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}
