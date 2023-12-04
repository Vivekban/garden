package com.garden.presentation.plantdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garden.data.common.Result
import com.garden.data.common.asResult
import com.garden.domain.plant.Plant
import com.garden.domain.plant.usecase.GetPlantUsecase
import com.garden.domain.plant.usecase.GetPlantUsecaseInput
import com.garden.domain.planting.usecase.AddPlantToGardenUsecase
import com.garden.domain.planting.usecase.AddPlantToGardenUsecaseUsecaseInput
import com.garden.domain.planting.usecase.IsPlantPlantedUsecase
import com.garden.domain.planting.usecase.IsPlantPlantedUsecaseInput
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The ViewModel used in PlantDetail.
 */
@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPlantUsecase: GetPlantUsecase,
    val isPlantPlantedUsecase: IsPlantPlantedUsecase,
    val addPlantToGardenUsecase: AddPlantToGardenUsecase
) : ViewModel() {

    /**
     * Plant's [_plantId] of which details will be shown.
     */
    private val _plantId: Int = savedStateHandle.get<Int>(PLANT_ID_SAVED_STATE_KEY)!!

    /**
     * Flow of plant's planted status.
     */
    private val _isPlantedFlow =
        isPlantPlantedUsecase(IsPlantPlantedUsecaseInput(_plantId))

    /**
     * Flow of plant's details based on [_plantId]
     */
    private val _plantFlow = getPlantUsecase(GetPlantUsecaseInput(_plantId)).asResult()

    /**
     * Flow of various [PlantDetailUiAction] that can be performed by user. It is listened by other
     * to make necessary based on user input
     */
    private val _uiActionFlow = MutableSharedFlow<PlantDetailUiAction>()

    /**
     * Flow of snack bar status. If true UI will display snack bar message
     */
    private val _showSnackBar =
        _uiActionFlow.filterIsInstance<PlantDetailUiAction.UpdateSnackBarState>()
            .distinctUntilChanged().map { it.show }

    /**
     * Expose [PlantDetailUiState]
     */
    val uiState = combine(_isPlantedFlow, _plantFlow, _showSnackBar) { isPlanted, plant, showBar ->
        PlantDetailUiState(isPlanted = isPlanted, plant = plant, showSnackBar = showBar)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = PlantDetailUiState()
    )

    fun addPlantToGarden() {
        viewModelScope.launch {
            addPlantToGardenUsecase(AddPlantToGardenUsecaseUsecaseInput(_plantId))
            _uiActionFlow.emit(PlantDetailUiAction.UpdateSnackBarState(true))
        }
    }

    fun dismissSnackbar() {
        viewModelScope.launch {
            _uiActionFlow.emit(PlantDetailUiAction.UpdateSnackBarState(false))
        }
    }

    companion object {
        private const val PLANT_ID_SAVED_STATE_KEY = "plantId"
    }

    /**
     *  Contains various UI actions can be performed.
     */
    sealed interface PlantDetailUiAction {
        /**
         * Used for showing/hiding snack bar
         */
        data class UpdateSnackBarState(val show: Boolean) : PlantDetailUiAction
    }

    /**
     *  State of Plant Detail
     */
    data class PlantDetailUiState(
        val showSnackBar: Boolean = false,
        val isPlanted: Boolean = false,
        val plant: Result<Plant> = Result.Loading
    )
}
