package com.garden.domain.plant.usecase

import com.garden.domain.common.Usecase
import com.garden.domain.plant.Plant
import com.garden.domain.plant.PlantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

data class GetPlantUsecaseInput(val id: Int)
class GetPlantUsecase @Inject constructor(private val plantRepository: PlantRepository) :
    Usecase<GetPlantUsecaseInput, Flow<Plant>> {
    override fun invoke(input: GetPlantUsecaseInput): Flow<Plant> {
        return plantRepository.getPlant(input.id)
    }
}
