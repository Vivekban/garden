package com.garden.domain.planting.usecase

import com.garden.domain.common.Usecase
import com.garden.domain.planting.GardenPlantingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

data class IsPlantPlantedUsecaseInput(val plantId: Int)
class IsPlantPlantedUsecase @Inject constructor(
    private val plantRepository: GardenPlantingRepository
) :
    Usecase<IsPlantPlantedUsecaseInput, Flow<Boolean>> {
    override fun invoke(input: IsPlantPlantedUsecaseInput): Flow<Boolean> {
        return plantRepository.isPlanted(input.plantId)
    }
}
