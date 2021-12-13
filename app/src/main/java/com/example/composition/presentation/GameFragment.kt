package com.example.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import java.lang.RuntimeException


class GameFragment : Fragment() {

    // 1 way Jetpack Nav прием параметра
    private val args by navArgs<GameFragmentArgs>() // также как и lazy проинициализируется


    // Подключаем VieModel // lazy - при первом обращение код проанализируется
    private val viewModelFactory by lazy {
        // 2 way Jetpack Nav прием параметра
        // val args = GameFragmentArgs.fromBundle(requireArguments())
        GameViewModelFactory( args.level, requireActivity().application)
    }

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    // так как с кнопками придется работать не один раз делаем коллекцию из кнопок
    private val tvOptions by lazy { // lazy - при первом обращение код проанализируется (И к этому моменту фрагмент уже будет собран когда будем обращаться)
        mutableListOf<TextView>().apply{
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    // Чтобы к элемнтам через binding нельзя было обращаться в не разрешенных Жизненых Цыклах
    // Используем метод Ниже через _binding переменую и binding + get
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setClickListenersToOptions()
    }

    private fun setClickListenersToOptions(){
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener{
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }


    private fun observeViewModel(){
        // в observe указываем параметр - viewLifecycleOwner
        viewModel.question.observe(viewLifecycleOwner){
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            for(i in 0 until tvOptions.size){
                tvOptions[i].text = it.options[i].toString()
            }
        }
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner){
            binding.progressBar.setProgress(it, true) // Анимация возможно только с Api 24 верссии Андроида
        }
        viewModel.enoughCount.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.setTextColor(getColorByState(it)) // Код обернули в функцию getColorByState(it)
        }
        viewModel.enoughPercent.observe(viewLifecycleOwner){
            val color = getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        viewModel.formattedTime.observe(viewLifecycleOwner){
            binding.tvTimer.text = it
        }
        viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner){
            launchGameFinishedFragment(it) // Функция Вызова Следующего фрагмента GameFinishedFragment
        }
        viewModel.progressAnswers.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getColorByState(goodState: Boolean): Int{
        val colorResId = if(goodState){
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light

        }
        return ContextCompat.getColor(requireContext(),colorResId) // Вернет цвет в формате INT
    }

    // Функция Вызова Следующего фрагмента GameFinishedFragment
    private fun launchGameFinishedFragment(gameResult: GameResult){

        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )

    }
}
