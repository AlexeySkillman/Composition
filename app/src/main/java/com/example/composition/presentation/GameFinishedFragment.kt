package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import java.lang.RuntimeException


class GameFinishedFragment : Fragment() {

    // private lateinit var gameResult: GameResult

    // 1 way Jetpack Nav прием параметра
    private val args by navArgs<GameFinishedFragmentArgs>() // также как и lazy проинициализируется

    // Чтобы к элемнтам через binding нельзя было обращаться в не разрешенных Жизненых Цыклах
    // Используем метод Ниже через _binding переменую и binding + get
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        // Передача в активити нужного CallBack для BackStack
         setupClickListeners()
    }

    // Передача в активити нужного CallBack для BackStack
    private fun setupClickListeners(){
        binding.buttonRetry.setOnClickListener(){
            retryGame()
        }
    }

    private fun bindViews(){
        with(binding){
            emojiResult.setImageResource(getSmileResId())
            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                args.gameResult.gameSettings.minCountOfRightAnswers
            )
            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                args.gameResult.countOfRightAnswer
            )
            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                args.gameResult.gameSettings.minPercentOfRightAnswers
            )
            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                getPercentOfRightAnswers()
            )
        }
    }

    // Функция для установки нужного BackStack
    private fun retryGame() {
        findNavController().popBackStack() // JepPuck Navigation
    }

    private fun getPercentOfRightAnswers() = with(args.gameResult){
        if(countOfQuestions == 0){
            0
        } else {
            ((countOfRightAnswer / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun getSmileResId() : Int{
        return if(args.gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
