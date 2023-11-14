package com.garden.domain.usecase.planting

import androidx.paging.PagingData
import com.garden.common.Usecase
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.repository.GardenPlantingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetPlantAndPlantingUsecaseInput(val query: String)
class GetPlantAndPlantingUsecase @Inject constructor(private val plantRepository: GardenPlantingRepository) :
    Usecase<GetPlantAndPlantingUsecaseInput, Flow<PagingData<PlantAndPlantings>>> {
    override fun invoke(input: GetPlantAndPlantingUsecaseInput): Flow<PagingData<PlantAndPlantings>> {
        return plantRepository.getPlantedGardens(input.query)
    }
}