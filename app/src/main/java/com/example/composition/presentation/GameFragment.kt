package com.example.composition.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException

class GameFragment : Fragment() {

    private lateinit var level: Level

    // Чтобы к элемнтам через binding нельзя было обращаться в не разрешенных Жизненых Цыклах
    // Используем метод Ниже через _binding переменую и binding + get
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvOption1.setOnClickListener(){
            launchGameFinishedFragment(
                GameResult(
                    true,
                    0,
                    0,
                    GameSettings(0,0,0,0)
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs(){
        level = requireArguments().getSerializable(KEY_LEVEL) as Level // Получаем обьект в виде Serializable getSerializable
        // Log.d("FragmentGameBinding", level.toString())
    }

    // Функция Вызова Следующего фрагмента GameFinishedFragment
    private fun launchGameFinishedFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {

        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"

        // Функция для Вызова Текущего Фрагмента в предыдущем фрагменте (В данном случаи не передаются параметры)
        // Чтобы в проекте был одинаковый подход и вбудущем позволит передавать параметры
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_LEVEL, level) // Передаем обьект в виде Serializable putSerializable
                }
            }
        }
    }
}
