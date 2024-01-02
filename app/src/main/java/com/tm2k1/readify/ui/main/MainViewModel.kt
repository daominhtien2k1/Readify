package com.tm2k1.readify.ui.main

import androidx.lifecycle.SavedStateHandle
import com.tm2k1.readify.base.viewmodel.BaseViewModel
import com.tm2k1.readify.base.viewmodel.ViewEffect
import com.tm2k1.readify.base.viewmodel.ViewState
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainViewState, MainViewEffect>(
    MainViewState.SampleState
) {
}

sealed class MainViewEffect: ViewEffect()

sealed class MainViewState: ViewState() {
    data object SampleState : MainViewState()
}
