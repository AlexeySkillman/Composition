package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter( "requaredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        count
    )
}
@BindingAdapter( "score")
fun bindScoreAnswers(textView: TextView, score: Int){
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        score
    )
}
@BindingAdapter( "requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percentage: Int){
    textView.text = String.format(
        textView.context.getString(R.string.required_percentage),
        percentage
    )
}
@BindingAdapter( "scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult){
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        getPercentOfRightAnswers(gameResult)
    )
}

private fun getPercentOfRightAnswers(gameResult: GameResult) = with(gameResult){
    if(countOfQuestions == 0){
        0
    } else {
        ((countOfRightAnswer / countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter( "resultEmoji")
fun bindResultEmoji(imageView: ImageView, winner: Boolean){
    imageView.setImageResource(getSmileResId(winner))
}


private fun getSmileResId(winner: Boolean) : Int{
    return if(winner) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_sad
    }
}