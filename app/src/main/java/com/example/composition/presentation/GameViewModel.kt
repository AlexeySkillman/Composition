package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

// Параметр application также передается в AndroidViewModel (Он должен быть типа Application )
class GameViewModel(application: Application): AndroidViewModel(application) { // Будем пользоваться контекстом, чтобы получить ресурсы


    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val context = application
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    // Переменные количество правильных и непраильных вопросов
    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    // На страте мы получаем Уровень (Level)
    fun  startGame(level: Level){
        getGameSettings(level) // Получаем Настройки Игры (Сложность и тд)
        startTimer() // Запускаем таймер
        generateQuestion() // Генерируем вопрос
    }
    // Вывели в отдельный метод настройки , чтобы не захламлять функцию startGame()
    private fun getGameSettings(level: Level){
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    // Когда получаем настройки нужно запустить таймер
    private fun startTimer(){ // вызовем его из VieModel
        timer = object : CountDownTimer(
            gameSettings.gameTimeIntSeconds * MILLIS_IN_SECONDS, // Получает Количесвто МилесекундСекунд
            MILLIS_IN_SECONDS // интервал
        ){
            override fun onTick(millisUntilFinished: Long) {
                // Форматируем время в понятный вид
                formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start() // Launch timer
    }
    // Даем пользователю выбирать ответы и проверяем его
    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    // START --- Следим за прогрессом
    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )
        _enoughPercent.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }
    // Вфчесляем процент правильных ответов
    private fun calculatePercentOfRightAnswers(): Int {
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }
    // END --- Следим за прогрессом



    // Проверяем ответ
    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    // После запуска таймера нужно сгенерировать вопрос
    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue) // в параметр передаем максимальное значение
    }

    // Функция для форматирования времени
    private fun formatTime(millisUntilFinished: Long): String{
        val second = millisUntilFinished / MILLIS_IN_SECONDS // получим полное число секунд например 345
        val minutes = second / SECONDS_IN_MINUTES
        val leftSecond = second - (minutes * SECONDS_IN_MINUTES) // в скобках получим ровное число например 300 итого (345 - 300) = 45 минут
        return String.format("%02d:%02d", minutes, leftSecond) // "%02d:%02d" означает что если будет 1 цыфра, то добавится ноль
    }

    // Когда игра заканчивается
    private fun finishGame(){
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    // Чтобы при переходе на другой фрагмент , таймер останавливался
    override fun onCleared() {
        super.onCleared()
        timer?.cancel() // Stop timer
    }

    companion object{
        private const val MILLIS_IN_SECONDS = 1000L // L тип LONG
        private const val SECONDS_IN_MINUTES = 60
    }


}