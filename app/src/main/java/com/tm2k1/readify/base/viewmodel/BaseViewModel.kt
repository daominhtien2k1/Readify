package com.tm2k1.readify.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<S : ViewState, E : ViewEffect>(
    uiState: S
) : ViewModel() {
    protected val TAG by lazy { this::class.simpleName }

    open val defaultDispatcher = Dispatchers.IO

    private val _stateFlow = MutableStateFlow(uiState)
    val stateFlow = _stateFlow.asStateFlow()

    private val _effectFlow = MutableSharedFlow<E>()
    val effectFlow = _effectFlow.asSharedFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    fun updateUIState(newUIState: S) {
        _stateFlow.value = newUIState
    }

    fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }

    fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effectFlow.emit(effect)
        }
    }

    val currentUIState = stateFlow.value

    fun launch(dispatcher: CoroutineDispatcher = defaultDispatcher, action: suspend () -> Unit) {
        viewModelScope.launch(dispatcher) {
            action.invoke()
        }
    }
}

open class ViewState
open class ViewEffect
