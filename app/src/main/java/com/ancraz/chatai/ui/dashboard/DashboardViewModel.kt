package com.ancraz.chatai.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ancraz.chatai.data.AppPrefs
import com.ancraz.chatai.data.backend.repository.AiApiRepositoryImpl
import com.ancraz.chatai.data.backend.superbase.models.ActivityDto
import com.ancraz.chatai.domain.useCase.GetActivitiesDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel: ViewModel() {

    private val getActivitiesDataUseCase by lazy {
        GetActivitiesDataUseCase(
            AiApiRepositoryImpl()
        )
    }

    private val _activitiesState = MutableStateFlow<List<ActivityDto>>(emptyList())
    val activitiesState = _activitiesState.asStateFlow()


    init {
        getUserActivities()
    }


    private fun getUserActivities(){
        viewModelScope.launch(Dispatchers.IO) {
            getActivitiesDataUseCase.invoke(AppPrefs.userId).collect{ activities ->
                _activitiesState.value = activities
            }
        }
    }
}