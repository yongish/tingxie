package com.zhiyong.tingxie.ui.exercises_completed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewExercisesCompletedBinding
import com.zhiyong.tingxie.network.NetworkExercisesCompleted

class ExercisesCompletedAdapter(
  private val context: Context,
  val viewModel: ExercisesCompletedViewModel,
  val recyclerView: RecyclerView
) : RecyclerView.Adapter<ExercisesCompletedAdapter.ViewHolder>() {
  var exerciseTypes = mutableListOf<NetworkExercisesCompleted>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewExercisesCompletedBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val exerciseType = exerciseTypes[position]
    holder.bind(exerciseType)
//    holder.btnView.setOnClickListener {
//    }
//    holder.btnPlay.setOnClickListener(context.startActivity())
  }

  override fun getItemCount(): Int = exerciseTypes.size

  class ViewHolder(private val binding: RecyclerviewExercisesCompletedBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val tvName = binding.tvName
    val btnView = binding.btnView
    val btnPlay = binding.btnPlay

    fun bind(exerciseType: NetworkExercisesCompleted) = with(binding) {
      tvName.text = exerciseType.name
    }
  }
}
