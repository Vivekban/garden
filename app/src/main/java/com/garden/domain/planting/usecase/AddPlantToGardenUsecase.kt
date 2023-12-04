package com.garden.domain.planting.usecase

import com.garden.domain.common.SuspendUsecase
import com.garden.domain.planting.GardenPlantingRepository
import java.io.IOException
import javax.inject.Inject

data class AddPlantToGardenUsecaseUsecaseInput(val plantId: Int)
class AddPlantToGardenUsecase @Inject constructor(
    private val plantRepository: GardenPlantingRepository
) :
    SuspendUsecase<AddPlantToGardenUsecaseUsecaseInput, Boolean> {
    override suspend fun invoke(input: AddPlantToGardenUsecaseUsecaseInput): Boolean {
        return try {
            plantRepository.createGardenPlanting(input.plantId)
            true
        } catch (e: IOException) {
            false
        }
    }
}
