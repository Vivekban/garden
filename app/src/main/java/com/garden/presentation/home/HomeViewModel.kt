package com.garden.presentation.home

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.garden.R
import com.garden.domain.plant.Plant
import com.garden.domain.plant.usecase.GetPlantsUsecase
import com.garden.domain.plant.usecase.GetPlantsUsecaseInput
import com.garden.domain.plantandplanting.PlantAndPlantings
import com.garden.domain.planting.usecase.GetPlantAndPlantingUsecase
import com.garden.domain.planting.usecase.GetPlantAndPlantingUsecaseInput
import com.garden.presentation.common.PresentationConstant
import com.garden.presentation.helper.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.annotation.concurrent.Immutable
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The ViewModel for HomeScreen.
 */
@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val getPlantsUsecase: GetPlantsUsecase,
    private val getPlantAndPlantingUsecase: GetPlantAndPlantingUsecase,
    networkMonitor: NetworkMonitor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _actionFlow = MutableSharedFlow<HomeUiAction>()

    private val _messageFlow = MutableSharedFlow<Int?>()

    private val _page =
        _actionFlow.filterIsInstance<HomeUiAction.PageChange>().distinctUntilChanged().onStart {
            emit(HomeUiAction.PageChange(GardenPage.MY_GARDEN))
        }

    private val _searches =
        _actionFlow.filterIsInstance<HomeUiAction.Search>().distinctUntilChanged().onStart {
            // emit initial query
            emit(
                HomeUiAction.Search(
                    savedStateHandle.get<String>(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
                )
            )
        }

    private val _enableSearching =
        _actionFlow.filterIsInstance<HomeUiAction.UpdateSearchState>().distinctUntilChanged()

    private val _isOnline = networkMonitor.isOnline.distinctUntilChanged()

    private val _allMessage = merge(
        _messageFlow,
        _isOnline.map { if (it) R.string.internet_connected else R.string.network_unavailable }
    )

    private val _state =
        combine(
            _page,
            _enableSearching,
            _searches,
            _allMessage
        ) { page, enableSearching, search, allMessage ->
            HomeUiState(
                query = if (enableSearching.enable) {
                    SearchingState.Ongoing(search.query)
                } else {
                    SearchingState.No
                },
                page.page,
                allMessage
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = HomeUiState()
        )

    /**
     * Represents HomeScreen Ui state
     */
    val state: StateFlow<HomeUiState> = _state

    /**
     *  Source of various available plants, listen to both [HomeUiAction.Search]
     *  and [HomeUiAction.PageChange] any changes on these will result on fetching items again.
     */
    val plantsPagingData: Flow<PagingData<Plant>> =
        _searches.debounce(PresentationConstant.SEARCH_DEBOUNCE_TIME_MS)
            .combine(_page, ::Pair)
            .filter { (_, page) -> page.page == GardenPage.PLANT_LIST }
            .flatMapLatest { (search, _) ->
                getPlants(search.query)
            }
            .cachedIn(viewModelScope)

    /**
     *  Source of plants added by garden, listen to both [HomeUiAction.Search]
     *  and [HomeUiAction.PageChange] any changes on these will result on fetching items again.
     */
    val plantAndPlantingDataSource: Flow<PagingData<PlantAndPlantings>> =
        _searches.debounce(PresentationConstant.SEARCH_DEBOUNCE_TIME_MS)
            .combine(_page, ::Pair)
            .filter { (_, page) -> page.page == GardenPage.MY_GARDEN }
            .flatMapLatest { (search, _) ->
                getPlanting(search.query)
            }.cachedIn(viewModelScope)

    private fun getPlanting(query: String): Flow<PagingData<PlantAndPlantings>> {
        return getPlantAndPlantingUsecase(GetPlantAndPlantingUsecaseInput(query))
    }

    private fun getPlants(query: String): Flow<PagingData<Plant>> {
        return getPlantsUsecase(GetPlantsUsecaseInput(query))
    }

    fun messageShown() {
        viewModelScope.launch {
            _messageFlow.emit(null)
        }
    }

    fun handleUiAction(uiAction: HomeUiAction) {
        viewModelScope.launch {
            _actionFlow.emit(uiAction)
        }
    }
}

sealed class HomeUiAction {
    data class UpdateSearchState(val enable: Boolean) : HomeUiAction()
    data class Search(val query: String) : HomeUiAction()
    data class PageChange(val page: GardenPage) : HomeUiAction()
}

@Immutable
data class HomeUiState(
    val query: SearchingState = SearchingState.No,
    val page: GardenPage = GardenPage.MY_GARDEN,
    @StringRes val message: Int? = null
) {
    fun searchQuery(): String? {
        return when (query) {
            SearchingState.No -> null
            is SearchingState.Ongoing -> query.query
        }
    }
}

private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = ""

/**
 *
 */
sealed class SearchingState {
    data class Ongoing(val query: String) : SearchingState()
    object No : SearchingState()
}
