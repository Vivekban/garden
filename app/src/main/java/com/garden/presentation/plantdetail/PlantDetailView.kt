package com.garden.presentation.plantdetail

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.garden.R
import com.garden.databinding.ItemPlantDescriptionBinding
import com.garden.domain.plant.Plant
import com.garden.presentation.common.PresentationConstant
import com.garden.presentation.common.VoidCallback
import com.garden.presentation.theme.Dimens
import com.garden.presentation.theme.visible
import com.garden.presentation.view.NetworkImage
import com.garden.presentation.view.TextSnackbarContainer
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * As these callbacks are passed in through multiple Composable, to avoid having to name
 * parameters to not mix them up, they're aggregated in this class.
 */
data class PlantDetailsCallbacks(
    val onFabClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onShareClick: (String) -> Unit,
    val onSupportCallClick: VoidCallback
)

@Composable
fun PlantDetailsScreen(
    plantDetailsViewModel: PlantDetailViewModel,
    onBackClick: VoidCallback,
    onShareClick: (String) -> Unit,
    onSupportCallClick: VoidCallback,
    modifier: Modifier = Modifier
) {
    val state by plantDetailsViewModel.uiState.collectAsStateWithLifecycle()
    val plant = state.plant
    val isPlanted = state.isPlanted
    val showSnackBar = state.showSnackBar

    val systemUiController = rememberSystemUiController()

    DisposableEffect(key1 = true) {
        systemUiController.isStatusBarVisible = false // Status bar
        onDispose {
            systemUiController.isStatusBarVisible = true // Status bar
        }
    }

    if (plant.getOrNull() != null) {
        Surface(modifier) {
            TextSnackbarContainer(
                snackbarText = stringResource(R.string.added_plant_to_garden),
                showSnackbar = showSnackBar,
                onDismissSnackbar = { plantDetailsViewModel.dismissSnackbar() }
            ) {
                PlantDetails(
                    plant.getOrNull()!!,
                    isPlanted,
                    PlantDetailsCallbacks(
                        onBackClick = onBackClick,
                        onFabClick = {
                            plantDetailsViewModel.addPlantToGarden()
                        },
                        onShareClick = onShareClick,
                        onSupportCallClick = onSupportCallClick
                    )
                )
            }
        }
    } else if (plant.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@VisibleForTesting
@Composable
fun PlantDetails(
    plant: Plant,
    isPlanted: Boolean,
    callbacks: PlantDetailsCallbacks,
    modifier: Modifier = Modifier
) {
    // PlantDetails owns the scrollerPosition to simulate CollapsingToolbarLayout's behavior
    val scrollState = rememberScrollState()
    var plantScroller by remember {
        mutableStateOf(PlantDetailsScroller(scrollState, Float.MIN_VALUE))
    }
    val transitionState =
        remember(plantScroller) { plantScroller.toolbarTransitionState }
    val toolbarState = plantScroller.getToolbarState(LocalDensity.current)

    // Transition that fades in/out the header with the image and the Toolbar
    val transition = updateTransition(transitionState, label = "")
    val toolbarAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 0f else 1f
    }
    val contentAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
        label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 1f else 0f
    }

    val toolbarHeightPx = with(LocalDensity.current) {
        Dimens.PlantDetailAppBarHeight.roundToPx().toFloat()
    }
    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue =
                    newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val showImagePreview = rememberSaveable { mutableStateOf(false) }

    if (showImagePreview.value) {
        FullScreenImage(
            plant = plant,
            onClick = {
                showImagePreview.value = false
            }
        )
    } else {
        Box(
            modifier
                .fillMaxSize()
                .systemBarsPadding()
                // attach as a parent to the nested scroll system
                .nestedScroll(nestedScrollConnection)
        ) {
            PlantDetailsContent(
                scrollState = scrollState,
                toolbarState = toolbarState,
                onNamePosition = { newNamePosition ->
                    // Comparing to Float.MIN_VALUE as we are just interested on the original
                    // position of name on the screen
                    if (plantScroller.namePosition == Float.MIN_VALUE) {
                        plantScroller =
                            plantScroller.copy(namePosition = newNamePosition)
                    }
                },
                plant = plant,
                isPlanted = isPlanted,
                imageHeight = with(LocalDensity.current) {
                    val candidateHeight =
                        Dimens.PlantDetailAppBarHeight + toolbarOffsetHeightPx.floatValue.toDp()
                    maxOf(candidateHeight, 1.dp)
                },
                onFabClick = callbacks.onFabClick,
                contentAlpha = { contentAlpha.value },
                onClick = {
                    showImagePreview.value = !showImagePreview.value
                }
            )
            PlantToolbar(
                toolbarState,
                plant.name,
                callbacks,
                toolbarAlpha = { toolbarAlpha.value },
                contentAlpha = { contentAlpha.value }
            )
        }
    }
}

@Composable
private fun PlantDetailsContent(
    scrollState: ScrollState,
    toolbarState: ToolbarState,
    plant: Plant,
    isPlanted: Boolean,
    imageHeight: Dp,
    onNamePosition: (Float) -> Unit,
    onFabClick: () -> Unit,
    onClick: () -> Unit,
    contentAlpha: () -> Float
) {
    Column(Modifier.verticalScroll(scrollState)) {
        ConstraintLayout {
            val (image, fab, info) = createRefs()

            PlantImage(
                imageUrl = plant.imageUrl ?: "",
                imageHeight = imageHeight,
                modifier = Modifier
                    .constrainAs(image) { top.linkTo(parent.top) }
                    .alpha(contentAlpha()),
                onClick = onClick
            )

            if (!isPlanted) {
                val fabEndMargin = Dimens.PaddingSmall
                PlantFab(
                    onFabClick = onFabClick,
                    modifier = Modifier
                        .constrainAs(fab) {
                            centerAround(image.bottom)
                            absoluteRight.linkTo(
                                parent.absoluteRight,
                                margin = fabEndMargin
                            )
                        }
                        .alpha(contentAlpha())
                )
            }

            PlantInformation(
                name = plant.name,
                wateringInterval = plant.wateringInterval,
                description = plant.description,
                onNamePosition = { onNamePosition(it) },
                toolbarState = toolbarState,
                modifier = Modifier.constrainAs(info) {
                    top.linkTo(image.bottom)
                }
            )
        }
    }
}

@Composable
fun FullScreenImage(plant: Plant, onClick: VoidCallback, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        var zoom by remember { mutableFloatStateOf(1f) }

        PlantImage(
            imageUrl = plant.imageUrl ?: "",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = zoom,
                    scaleY = zoom
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, _, gestureZoom, _ ->
                            zoom *= gestureZoom
                        }
                    )
                }
                .fillMaxSize(),
            contentScale = ContentScale.Inside
        )

        IconButton(
            onClick,
            Modifier
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = CircleShape
                )
                .padding(Dimens.PaddingExtraSmall)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.a11y_back)
            )
        }
    }
}

@Composable
private fun PlantImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    imageHeight: Dp? = null,
    placeholderColor: Color = MaterialTheme.colors.onSurface.copy(0.2f),
    contentScale: ContentScale = ContentScale.Crop,
    onClick: VoidCallback? = null
) {
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier
            .fillMaxWidth()
            .then(
                if (imageHeight != null) Modifier.height(imageHeight) else Modifier.fillMaxHeight()
            )
            .clickable {
                onClick?.invoke()
            }
    ) {
        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(placeholderColor)
            )
        }
        NetworkImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = contentScale
        ) {
            it.addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    isLoading = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    isLoading = false
                    return false
                }
            })
        }
    }
}

@Composable
private fun PlantFab(
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val addPlantContentDescription = stringResource(R.string.add_plant)
    FloatingActionButton(
        onClick = onFabClick,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.semantics {
            contentDescription = addPlantContentDescription
        }
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun PlantToolbar(
    toolbarState: ToolbarState,
    plantName: String,
    callbacks: PlantDetailsCallbacks,
    toolbarAlpha: () -> Float,
    contentAlpha: () -> Float
) {
    val onShareClick = {
        callbacks.onShareClick(plantName)
    }

    if (toolbarState.isShown) {
        PlantDetailsToolbar(
            modifier = Modifier.alpha(toolbarAlpha()),
            plantName = plantName,
            onBackClick = callbacks.onBackClick,
            onShareClick = onShareClick,
            onSupportCallClick = callbacks.onSupportCallClick
        )
    } else {
        PlantHeaderActions(
            modifier = Modifier.alpha(contentAlpha()),
            onBackClick = callbacks.onBackClick,
            onShareClick = onShareClick,
            onSupportCallClick = callbacks.onSupportCallClick
        )
    }
}

@Composable
private fun PlantDetailsToolbar(
    plantName: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onSupportCallClick: VoidCallback,
    modifier: Modifier = Modifier
) {
    Surface {
        TopAppBar(
            modifier = modifier.statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            IconButton(
                onBackClick,
                Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.a11y_back)
                )
            }
            Text(
                text = plantName,
                style = MaterialTheme.typography.h6,
                // As title in TopAppBar has extra inset on the left, need to do this: b/158829169
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
            val shareContentDescription =
                stringResource(R.string.menu_item_share_plant)
            val callContentDescription =
                stringResource(R.string.menu_item_call)
            IconButton(
                onSupportCallClick,
                Modifier
                    .align(Alignment.CenterVertically)
                    .semantics { contentDescription = callContentDescription }
            ) {
                Icon(
                    Icons.Filled.Call,
                    contentDescription = null
                )
            }
            IconButton(
                onShareClick,
                Modifier
                    .align(Alignment.CenterVertically)
                    .semantics { contentDescription = shareContentDescription }
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun PlantHeaderActions(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onSupportCallClick: VoidCallback,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(top = Dimens.ToolbarIconPadding)
    ) {
        val iconModifier = Modifier
            .sizeIn(
                maxWidth = Dimens.ToolbarIconSize,
                maxHeight = Dimens.ToolbarIconSize
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = Dimens.ToolbarIconPadding)
                .then(iconModifier)
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.a11y_back)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onSupportCallClick,
            modifier = Modifier
                .padding(end = Dimens.ToolbarIconPadding)
                .then(iconModifier)
        ) {
            Icon(
                Icons.Filled.Call,
                contentDescription = stringResource(R.string.menu_item_call)
            )
        }
        Spacer(modifier = Modifier.width(Dimens.ToolbarIconPadding))

        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .padding(end = Dimens.ToolbarIconPadding)
                .then(iconModifier)
        ) {
            Icon(
                Icons.Filled.Share,
                contentDescription = stringResource(R.string.menu_item_share_plant)
            )
        }
    }
}

@Composable
private fun PlantInformation(
    name: String,
    wateringInterval: Int,
    description: String,
    onNamePosition: (Float) -> Unit,
    toolbarState: ToolbarState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(Dimens.PaddingLarge)) {
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
                .align(Alignment.CenterHorizontally)
                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) }
                .visible { toolbarState == ToolbarState.HIDDEN }
        )
        Box(
            Modifier
                .align(Alignment.CenterHorizontally)
                .padding(
                    start = Dimens.PaddingSmall,
                    end = Dimens.PaddingSmall,
                    bottom = Dimens.PaddingNormal
                )
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.watering_needs_prefix),
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = Dimens.PaddingSmall)
                        .align(Alignment.CenterHorizontally)
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = pluralStringResource(
                            R.plurals.watering_needs_suffix,
                            wateringInterval,
                            wateringInterval
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        PlantDescription(description)
        PlantLocation()
    }
}

@Composable
private fun PlantDescription(description: String) {
    AndroidViewBinding(ItemPlantDescriptionBinding::inflate) {
        plantDescription.text = HtmlCompat.fromHtml(
            description,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        )
        plantDescription.movementMethod = LinkMovementMethod.getInstance()
        plantDescription.linksClickable = true
    }
}

@Composable
private fun PlantLocation() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(PresentationConstant.MISSISSAUGA, 10f)
    }
    Box(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(12.dp))
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            cameraPositionState = cameraPositionState
        ) {
            AdvancedMarker(
                state = MarkerState(position = PresentationConstant.MISSISSAUGA),
                title = "Marker in Mississauga"
            )
        }
    }
}

@Preview
@Composable
private fun PlantDetailContentPreview() {
    MdcTheme {
        Surface {
            PlantDetails(
                Plant(1, "Tomato", "HTML<br>description", 6),
                isPlanted = true,
                callbacks = PlantDetailsCallbacks({ }, { }, { }, {})
            )
        }
    }
}
