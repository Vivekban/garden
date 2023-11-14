package com.garden.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.garden.common.Constant
import com.garden.domain.model.Plant
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.usecase.plant.GetPlantsUsecase
import com.garden.domain.usecase.plant.GetPlantsUsecaseInput
import com.garden.domain.usecase.planting.GetPlantAndPlantingUsecase
import com.garden.domain.usecase.planting.GetPlantAndPlantingUsecaseInput
import com.garden.presentation.home.GardenPage
import dagger.hilt.android.lifecycle.HiltViewModel
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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for HomeSceen.
 */
@HiltViewModel
class HomeViewModel @Inject internal constructor(
    private val getPlantsUsecase: GetPlantsUsecase,
    private val getPlantAndPlantingUsecase: GetPlantAndPlantingUsecase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _actionFlow = MutableSharedFlow<HomeUiAction>()

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

    private val _state =
        combine(_page, _enableSearching, _searches) { page, enableSearching, search ->
            HomeUiState(
                query = if (enableSearching.enable) SearchingState.Ongoing(search.query) else SearchingState.No,
                page.page
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = HomeUiState()
        )

    val handleUiAction: (HomeUiAction) -> Unit = {
        viewModelScope.launch {
            _actionFlow.emit(it)
        }
    }

    val state: StateFlow<HomeUiState> = _state

    /**
     *  Source of various available plants, listen to both [HomeUiAction.Search]
     *  and [HomeUiAction.PageChange] any changes on these will result on fetching items again.
     */
    val plantsPagingData: Flow<PagingData<Plant>> =
        _searches.debounce(Constant.SEARCH_DEBOUNCE_TIME_MS)
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
        _searches.debounce(Constant.SEARCH_DEBOUNCE_TIME_MS)
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
}

sealed class HomeUiAction {
    data class UpdateSearchState(val enable: Boolean) : HomeUiAction()
    data class Search(val query: String) : HomeUiAction()
    data class PageChange(val page: GardenPage) : HomeUiAction()
}

data class HomeUiState(
    val query: SearchingState = SearchingState.No,
    val page: GardenPage = GardenPage.MY_GARDEN
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
