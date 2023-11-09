package com.garden.data.repository

import com.garden.data.database.dao.PlantingDao
import com.garden.data.entity.PlantingEntity
import com.garden.data.mappers.toEntity
import com.garden.data.mappers.toModel
import com.garden.domain.model.Planting
import com.garden.domain.repository.GardenPlantingRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GardenPlantingRepositoryImpl @Inject constructor(
    private val plantingDao: PlantingDao
) : GardenPlantingRepository {

    override suspend fun createGardenPlanting(plantId: String) {
        val planting = PlantingEntity(0, plantId)
        plantingDao.insertGardenPlanting(planting)
    }

    override suspend fun removeGardenPlanting(planting: Planting) {
        plantingDao.deleteGardenPlanting(planting.toEntity())
    }

    override fun isPlanted(plantId: String) =
        plantingDao.isPlanted(plantId)

    override fun getPlantedGardens() =
        plantingDao.getPlantedGardens().map { list -> list.map { item -> item.toModel() } }

}

