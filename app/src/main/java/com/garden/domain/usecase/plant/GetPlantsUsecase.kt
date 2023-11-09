package com.garden.domain.usecase.plant

import androidx.paging.PagingData
import com.garden.common.Usecase
import com.garden.domain.model.Plant
import com.garden.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetPlantsUsecaseInput(val query: String)
class GetPlantsUsecase @Inject constructor(private val plantRepository: PlantRepository) :
    Usecase<GetPlantsUsecaseInput, Flow<PagingData<Plant>>> {
    override fun invoke(input: GetPlantsUsecaseInput): Flow<PagingData<Plant>> {
        return plantRepository.getPlants(input.query)
    }
}