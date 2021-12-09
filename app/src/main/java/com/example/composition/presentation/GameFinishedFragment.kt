package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult
import java.lang.RuntimeException

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    // Чтобы к элемнтам через binding нельзя было обращаться в не разрешенных Жизненых Цыклах
    // Используем метод Ниже через _binding переменую и binding + get
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Передача в активити нужного CallBack для BackStack
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.buttonRetry.setOnClickListener(){
            retryGame()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs(){
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let{
            gameResult = it
        } // Получаем обьект в виде Parcelable getParcelable
    }

    // Функция для установки нужного BackStack
    private fun retryGame() {
        // requireActivity().supportFragmentManager.popBackStack(ChooseLevelFragment.NAME, 0) // Возвращает к данному фрагменту при press BackStack (flag 0)
        requireActivity().supportFragmentManager.popBackStack(GameFragment.NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE ) // Возвращает к данному фрагменту при press BackStack и удаляет его тоже (Поэтому вернет предыдущий) (flag - FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    companion object {
        private const val KEY_GAME_RESULT = "game_result"

        // Функция для Вызова Текущего Фрагмента в предыдущем фрагменте (В данном случаи передаются параметры)
        // Чтобы в проекте был одинаковый подход и вбудущем позволит передавать параметры
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return  GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult) // Передаем обьект в виде Parcelable putParcelable
                }
            }
        }
    }

}
