package ru.sumin.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.composition.databinding.FragmentChooseLevelBinding
import java.lang.RuntimeException

class ChooseLevelFragment : Fragment() {

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
