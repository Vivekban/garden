package com.garden.domain.plant.usecase

import androidx.paging.PagingData
import com.garden.domain.common.Usecase
import com.garden.domain.plant.Plant
import com.garden.domain.plant.PlantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

data class GetPlantsUsecaseInput(val query: String)
class GetPlantsUsecase @Inject constructor(private val plantRepository: PlantRepository) :
    Usecase<GetPlantsUsecaseInput, Flow<PagingData<Plant>>> {
    override fun invoke(input: GetPlantsUsecaseInput): Flow<PagingData<Plant>> {
        return plantRepository.getPlants(input.query)
    }
}
