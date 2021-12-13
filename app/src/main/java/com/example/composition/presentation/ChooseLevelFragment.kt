package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentChooseLevelBinding
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException

class ChooseLevelFragment : Fragment() {

    private lateinit var level: Level

    // Чтобы к элемнтам через binding нельзя было обращаться в не разрешенных Жизненых Цыклах
    // Используем метод Ниже через _binding переменую и binding + get
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            buttonLevelTest.setOnClickListener(){
                launchGameFragment(com.example.composition.domain.entity.Level.TEST)
            }
            buttonLevelEasy.setOnClickListener(){
                launchGameFragment(com.example.composition.domain.entity.Level.EASY)
            }
            buttonLevelNormal.setOnClickListener(){
                launchGameFragment(com.example.composition.domain.entity.Level.NORMAL)
            }
            buttonLevelHard.setOnClickListener(){
                launchGameFragment(com.example.composition.domain.entity.Level.HARD)
            }
        }

    }

    // Функция Вызова Следующего фрагмента GameFragment
    private fun launchGameFragment(level: Level){

        // Параметры передаем через Jetpack Navigation
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragment2ToGameFragment(level)
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
