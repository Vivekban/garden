package com.garden.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.garden.data.repository.GardenPlantingRepositoryImpl
import com.garden.domain.model.PlantAndPlantings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GardenPlantingListViewModel @Inject internal constructor(
    gardenPlantingRepository: GardenPlantingRepositoryImpl
) : ViewModel() {
    val plantAndPlantings: Flow<List<PlantAndPlantings>> =
        gardenPlantingRepository.getPlantedGardens()
}