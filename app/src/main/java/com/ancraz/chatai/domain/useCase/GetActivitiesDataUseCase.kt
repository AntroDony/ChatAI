package com.ancraz.chatai.domain.useCase

import com.ancraz.chatai.common.utils.debugLog
import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import com.ancraz.chatai.domain.repository.AiApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetActivitiesDataUseCase(
    private val aiApiRepository: AiApiRepository
) {

    fun invoke(userId: String): Flow<List<ActivityDto>>{
        return flow {
            aiApiRepository.getActivitiesByUser(userId).collect{ activities ->
                emit(activities)
            }
        }
    }
}