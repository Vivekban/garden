package com.garden.presentation.plantdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.garden.data.common.asResult
import com.garden.data.plant.PlantRepositoryImpl
import com.garden.data.planting.PlantingRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * The ViewModel used in PlantDetail.
 */
@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    plantRepository: PlantRepositoryImpl,
    private val gardenPlantingRepository: PlantingRepositoryImpl
) : ViewModel() {

    val plantId: Int = savedStateHandle.get<Int>(PLANT_ID_SAVED_STATE_KEY)!!

    val isPlanted = gardenPlantingRepository.isPlanted(plantId)
    val plant = plantRepository.getPlant(plantId).asResult().asLiveData()

    private val _showSnackBar = MutableLiveData(false)
    val showSnackBar: LiveData<Boolean>
        get() = _showSnackBar

    fun addPlantToGarden() {
        viewModelScope.launch {
            gardenPlantingRepository.createGardenPlanting(plantId)
            _showSnackBar.value = true
        }
    }

    fun dismissSnackbar() {
        _showSnackBar.value = false
    }

    companion object {
        private const val PLANT_ID_SAVED_STATE_KEY = "plantId"
    }
}
