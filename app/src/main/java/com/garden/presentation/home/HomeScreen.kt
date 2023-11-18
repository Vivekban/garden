package com.garden.presentation.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.garden.R
import com.garden.common.VoidCallback
import com.garden.domain.model.Plant
import com.garden.domain.model.PlantAndPlantings
import com.garden.presentation.garden.GardenScreen
import com.garden.presentation.plantlist.PlantListScreen
import com.garden.presentation.viewmodels.HomeUiAction
import com.garden.presentation.viewmodels.HomeViewModel
import com.garden.presentation.viewmodels.SearchingState
import com.google.accompanist.themeadapter.material.MdcTheme
import kotlinx.coroutines.launch

/**
 * Callback to show snackbar
 */
typealias ShowSnackBar = suspend (message: String, action: String?) -> SnackbarResult

enum class GardenPage(
    @StringRes val titleResId: Int,
    @DrawableRes val drawableResId: Int
) {
    MY_GARDEN(R.string.my_garden_title, R.drawable.ic_my_garden_active),
    PLANT_LIST(R.string.plant_list_title, R.drawable.ic_plant_list_active)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPlantClick: (Plant) -> Unit = {},
    onPageChange: (GardenPage) -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState { GardenPage.values().size }

    val uiState by homeViewModel.state.collectAsState()

    val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`

    val info = uiState.message?.let { stringResource(it) }
    LaunchedEffect(key1 = info) {
        info?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it
            )
            homeViewModel.messageShown()
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            HomeTopAppBar(
                isSearching = uiState.query is SearchingState.Ongoing,
                searchQuery = uiState.searchQuery(),
                onSearchClick = {
                    homeViewModel.handleUiAction(HomeUiAction.UpdateSearchState(true))
                },
                onSearchQueryChanged = { query ->
                    homeViewModel.handleUiAction(HomeUiAction.Search(query))
                },
                onSearchTriggered = { query ->
                    homeViewModel.handleUiAction(HomeUiAction.Search(query))
                },
                onBackClick = {
                    // resetting search
                    homeViewModel.handleUiAction(HomeUiAction.Search(""))
                    homeViewModel.handleUiAction(HomeUiAction.UpdateSearchState(false))
                }

            )
        }
    ) { contentPadding ->
        HomePagerScreen(
            modifier = Modifier.padding(top = contentPadding.calculateTopPadding()),
            onPlantClick = onPlantClick,
            onPageChange = onPageChange,
            viewModel = homeViewModel,
            pagerState = pagerState,
            clearSearch = {
                homeViewModel.handleUiAction(HomeUiAction.Search(""))
            },
            showSnackBar = { message, action ->
                scaffoldState.snackbarHostState.showSnackbar(message, action)
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    pagerState: PagerState,
    clearSearch: VoidCallback,
    onPlantClick: (Plant) -> Unit,
    onPageChange: (GardenPage) -> Unit,
    modifier: Modifier = Modifier,
    pages: Array<GardenPage> = GardenPage.values(),
    viewModel: HomeViewModel = hiltViewModel(),
    showSnackBar: ShowSnackBar? = null
) {
    val gardenPlants = viewModel.plantAndPlantingDataSource.collectAsLazyPagingItems()
    val plants = viewModel.plantsPagingData.collectAsLazyPagingItems()
    val uiState by viewModel.state.collectAsState()

    HomePagerScreen(
        modifier = modifier,
        pages = pages,
        gardenPlants = gardenPlants,
        plants = plants,
        pagerState = pagerState,
        searchQuery = uiState.searchQuery(),
        clearSearch = clearSearch,
        onPlantClick = onPlantClick,
        onPageChange = {
            onPageChange(it)
            viewModel.handleUiAction(HomeUiAction.PageChange(it))
        },
        showSnackBar = showSnackBar
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    gardenPlants: LazyPagingItems<PlantAndPlantings>,
    plants: LazyPagingItems<Plant>,
    pagerState: PagerState,
    searchQuery: String?,
    clearSearch: VoidCallback,
    onPlantClick: (Plant) -> Unit,
    onPageChange: (GardenPage) -> Unit,
    modifier: Modifier = Modifier,
    pages: Array<GardenPage> = GardenPage.values(),
    showSnackBar: ShowSnackBar? = null
) {
    LaunchedEffect(pagerState.currentPage) {
        onPageChange(pages[pagerState.currentPage])
    }

    Column(modifier.nestedScroll(rememberNestedScrollInteropConnection())) {
        val coroutineScope = rememberCoroutineScope()

        // Pages
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { index ->
            when (pages[index]) {
                GardenPage.MY_GARDEN -> {
                    GardenScreen(
                        gardenPlants = gardenPlants,
                        Modifier.fillMaxSize(),
                        searchQuery = searchQuery,
                        onAddPlantClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(GardenPage.PLANT_LIST.ordinal)
                            }
                        },
                        onPlantClick = {
                            onPlantClick(it.plant)
                        },
                        onClearSearchClick = clearSearch,
                        showSnackBar = showSnackBar
                    )
                }

                GardenPage.PLANT_LIST -> {
                    PlantListScreen(
                        plants = plants,
                        onPlantClick = onPlantClick,
                        modifier = Modifier.fillMaxSize(),
                        searchQuery = searchQuery,
                        onClearSearchClick = clearSearch,
                        showSnackBar = showSnackBar
                    )
                }
            }
        }

        // Tab Row
        TabRow(selectedTabIndex = pagerState.currentPage) {
            pages.forEachIndexed { index, page ->
                val title = stringResource(id = page.titleResId)
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(text = title) },
                    icon = {
                        Icon(
                            painter = painterResource(id = page.drawableResId),
                            contentDescription = title
                        )
                    },
                    unselectedContentColor = MaterialTheme.colors.primaryVariant,
                    selectedContentColor = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
private fun HomeTopAppBar(
    isSearching: Boolean,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String?,
    onSearchTriggered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isSearching) {
        SearchToolbar(
            modifier = modifier.statusBarsPadding(),
            onBackClick = onBackClick,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchTriggered = onSearchTriggered,
            searchQuery = searchQuery ?: ""
        )
    } else {
        TopAppBar(
            title = {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name)
                    )
                }
            },
            modifier.statusBarsPadding(),
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(
                            id = R.string.menu_search_plant
                        ),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            },
            elevation = 0.dp
        )
    }
}

@Composable
private fun SearchToolbar(
    onBackClick: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchQuery: String = ""
) {
    Surface(color = MaterialTheme.colors.primary) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(
                        id = R.string.back
                    ),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            SearchTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                onSearchQueryChanged = onSearchQueryChanged,
                onSearchTriggered = onSearchTriggered,
                searchQuery = searchQuery
            )
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(
                            id = R.string.clear_search_text_content_desc
                        ),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchTextField(
    onSearchQueryChanged: (String) -> Unit,
    searchQuery: String,
    onSearchTriggered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    BasicTextField(
        value = searchQuery,
        onValueChange = {
            if (!it.contains("\n")) {
                onSearchQueryChanged(it)
            }
        },
        cursorBrush = SolidColor(MaterialTheme.colors.onPrimary),
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onPrimary),
        modifier = modifier
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            }
        ),
        maxLines = 1,
        singleLine = true
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    MdcTheme {
        SearchToolbar(
            onBackClick = {},
            onSearchQueryChanged = {},
            onSearchTriggered = {}
        )
    }
}

// @OptIn(ExperimentalFoundationApi::class)
// @Preview
// @Composable
// private fun HomeScreenPreview(
//    @PreviewParameter(HomeScreenPreviewParamProvider::class) param: HomePreviewParam
// ) {
//    MdcTheme {
//        val pagerState = rememberPagerState { GardenPage.values().size }
//        HomePagerScreen(
//            onPlantClick = {},
//            onPageChange = {},
//            gardenPlants = param.gardenPlants,
//            plants = param.plants,
//            pagerState = pagerState
//        )
//    }
// }

private data class HomePreviewParam(
    val gardenPlants: List<PlantAndPlantings>,
    val plants: List<Plant>
)

private class HomeScreenPreviewParamProvider : PreviewParameterProvider<HomePreviewParam> {
    override val values: Sequence<HomePreviewParam> =
        sequenceOf(
            HomePreviewParam(
                gardenPlants = emptyList(),
                plants = emptyList()
            ),
            HomePreviewParam(
                gardenPlants = emptyList(),
                plants = listOf(
                    Plant(1, "Apple", "Apple", growZoneNumber = 1),
                    Plant(2, "Banana", "Banana", growZoneNumber = 2),
                    Plant(3, "Carrot", "Carrot", growZoneNumber = 3),
                    Plant(4, "Dill", "Dill", growZoneNumber = 3)
                )
            )
        )
}
