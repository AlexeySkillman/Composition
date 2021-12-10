package com.example.composition.domain.usecases

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    // Пишется вместе operator fun invoke - оператор invoke
    // Это дает возможность вызывать класс как функцию GetGameSettingsUseCase()
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}