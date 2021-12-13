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

        binding.gameResult = args.gameResult
        // Передача в активити нужного CallBack для BackStack
         setupClickListeners()
    }

    // Передача в активити нужного CallBack для BackStack
    private fun setupClickListeners(){
        binding.buttonRetry.setOnClickListener(){
            retryGame()
        }
    }

    // Функция для установки нужного BackStack
    private fun retryGame() {
        findNavController().popBackStack() // JepPuck Navigation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
