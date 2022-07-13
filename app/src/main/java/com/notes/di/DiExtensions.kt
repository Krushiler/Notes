package com.notes.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notes.ui._base.AssistedViewModelFactory

inline fun <reified T : ViewModel> Fragment.provideAssistedViewModel(
    noinline create: (stateHandle: SavedStateHandle) -> T
): T {
    return ViewModelProvider(this, AssistedViewModelFactory(this, create))[T::class.java]
}