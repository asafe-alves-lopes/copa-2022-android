package me.dio.copa.catar.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import me.dio.copa.catar.domain.model.Match
import me.dio.copa.catar.domain.usecase.GetMatchesUseCase
import javax.inject.Inject

sealed interface MainState {
    data class Success(val matches: List<Match>) : MainState
    data class Error(val error: MainErrors) : MainState
    object Loading : MainState
    object Empty : MainState
}

sealed interface MainErrors {
    object LoadMatches : MainErrors
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Empty)
    val state: StateFlow<MainState> = _state

    init {
        getMatches()
    }

    fun getMatches() = viewModelScope.launch {
        try {
            getMatchesUseCase()
                .flowOn(Dispatchers.IO)
                .onStart {
                    _state.value = MainState.Loading
                }
                .collect {
                    _state.value =
                    if(it.isNotEmpty())
                        MainState.Success(it)
                    else
                        MainState.Empty
                }
        } catch (exception: Exception) {
            _state.value = MainState.Error(MainErrors.LoadMatches)
        }
    }
}