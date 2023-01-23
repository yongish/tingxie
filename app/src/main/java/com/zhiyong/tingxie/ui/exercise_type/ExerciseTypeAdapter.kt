package com.zhiyong.tingxie.ui.exercise_type

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewExerciseTypeBinding

class ExerciseTypeAdapter(private val context: Context, val viewModel: ExerciseTypeViewModel, val recyclerView: RecyclerView) : RecyclerView.Adapter<ExerciseTypeAdapter.ViewHolder> {

  class ViewHolder(private val binding: RecyclerviewExerciseTypeBinding) : RecyclerView.ViewHolder(binding.root) {
    val btnView = binding.btnView
    val btnPlay = binding.btnPlay
  }
}
