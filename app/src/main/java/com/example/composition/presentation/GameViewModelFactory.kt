package com.example.composition.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.composition.domain.entity.Level

// Если мы хотим передать во ViewModel какие нибудь параметры то
// используем ViewModelFactory
class GameViewModelFactory(
    private val level: Level,
    private val application: Application // Context нельязя передавать во ViewModel поэтому передаем Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(level, application) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }

}