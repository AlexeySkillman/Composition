package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.composition.R
import com.example.composition.databinding.FragmentWelcomeBinding
import java.lang.RuntimeException


class WelcomeFragment : Fragment() {

    // Чтобы к элемнтам через binding нельзя было обращаться в не разрешенных Жизненых Цыклах
    // Используем метод Ниже через _binding переменую и binding + get
    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonUnderstand.setOnClickListener(){
            launchChooseLevelFragment()
        }
    }

    // Функция Вызова Следующего фрагмента ChooseLevelFragment
    private fun launchChooseLevelFragment(){
        // Jetpack Navigation
        findNavController().navigate(R.id.action_welcomeFragment_to_chooseLevelFragment2)

        /*
            До  Jetpack Navigation
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, ChooseLevelFragment.newInstance()) // ChooseLevelFragment.newInstance() - Чтобы в проекте был одинаковый подход и вбудущем позволит передавать параметры
                .addToBackStack(ChooseLevelFragment.NAME) // При открытие Фрагмента мы добавляем BacckStack c именем ChooseLevelFragment.NAME чтобы создать точку к которой можно было бы возвращаться
                .commit()
        */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
