package com.garden.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.garden.domain.model.Plant
import com.garden.domain.usecase.plant.GetPlantsUsecase
import com.garden.domain.usecase.plant.GetPlantsUsecaseInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for plant list.
 */
@HiltViewModel
class PlantListViewModel @Inject internal constructor(
    private val getPlantsUsecase: GetPlantsUsecase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _actionFlow = MutableSharedFlow<PlantListUiAction>()

    private val _searches =
        _actionFlow.filterIsInstance<PlantListUiAction.Search>().distinctUntilChanged().onStart {
            // emit initial query
            emit(
                PlantListUiAction.Search(
                    savedStateHandle.get<String>(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
                )
            )
        }

    private val _queriesScrolled = _actionFlow
        .filterIsInstance<PlantListUiAction.Scroll>()
        .distinctUntilChanged()
        // This is shared to keep the flow "hot" while caching the last query scrolled,
        // otherwise each flatMapLatest invocation would lose the last query scrolled,
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 1
        )
        .onStart {
            emit(
                PlantListUiAction.Scroll(
                    currentQuery = savedStateHandle.get<String>(
                        LAST_QUERY_SCROLLED
                    ) ?: DEFAULT_QUERY
                )
            )
        }

    private val _state = combine(_searches, _queriesScrolled, ::Pair).map { (search, scroll) ->
        PlantListUiState(
            query = search.query,
            lastQueryScrolled = scroll.currentQuery,
            hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = PlantListUiState()
    )

    val performAction: (PlantListUiAction) -> Unit = {
        viewModelScope.launch {
            _actionFlow.emit(it)
        }
    }

    val state: StateFlow<PlantListUiState> = _state

    val pagingDataSource: Flow<PagingData<Plant>> = _searches.flatMapLatest {
        getPlants(it.query)
    }.cachedIn(viewModelScope)


    private fun getPlants(query: String): Flow<PagingData<Plant>> {
        return getPlantsUsecase(GetPlantsUsecaseInput(query))
    }

}

sealed class PlantListUiAction {
    data class Search(var query: String) : PlantListUiAction()

    data class Scroll(var currentQuery: String) : PlantListUiAction()
}

data class PlantListUiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"
private const val LAST_SEARCH_QUERY: String = "last_search_query"
private const val DEFAULT_QUERY = ""