package com.garden.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.garden.common.asResult
import com.garden.data.repository.GardenPlantingRepositoryImpl
import com.garden.data.repository.PlantRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel used in PlantDetail.
 */
@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    plantRepository: PlantRepositoryImpl,
    private val gardenPlantingRepository: GardenPlantingRepositoryImpl
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
