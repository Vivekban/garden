package com.garden.domain.planting.usecase

import androidx.paging.PagingData
import com.garden.domain.common.Usecase
import com.garden.domain.plantandplanting.PlantAndPlantings
import com.garden.domain.planting.GardenPlantingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

data class GetPlantAndPlantingUsecaseInput(val query: String)
class GetPlantAndPlantingUsecase @Inject constructor(
    private val plantRepository: GardenPlantingRepository
) :
    Usecase<GetPlantAndPlantingUsecaseInput, Flow<PagingData<PlantAndPlantings>>> {
    override fun invoke(input: GetPlantAndPlantingUsecaseInput):
        Flow<PagingData<PlantAndPlantings>> {
        return plantRepository.getPlantedGardens(input.query)
    }
}
