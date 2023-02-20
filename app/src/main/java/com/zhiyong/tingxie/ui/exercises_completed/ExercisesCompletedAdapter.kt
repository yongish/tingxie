package com.zhiyong.tingxie.ui.exercises_completed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewExercisesCompletedBinding
import com.zhiyong.tingxie.network.NetworkExercisesCompleted
import com.zhiyong.tingxie.ui.read_then_write.ReadThenWriteActivity
import com.zhiyong.tingxie.ui.readings.ReadingsActivity
import java.util.*

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

    holder.btnView.setOnClickListener {
      context.startActivity(
        Intent(
          context, when (exerciseType.englishName) {
            "readThenWrite" -> ReadThenWriteActivity::class.java
            "reading" -> ReadingsActivity::class.java
            else -> ReadThenWriteActivity::class.java
          }
        )
      )
    }
  }

  override fun getItemCount(): Int = exerciseTypes.size

  class ViewHolder(private val binding: RecyclerviewExercisesCompletedBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val btnView = binding.btnView
    val btnPlay = binding.btnPlay

    fun bind(exercisesCompleted: NetworkExercisesCompleted) = with(binding) {
      tvName.text = exercisesCompleted.chineseName
      tvCompleted.text = String.format(
        Locale.US,
        "%d/%d exercises complete",
        exercisesCompleted.completedCount,
        exercisesCompleted.totalCount
      )
    }
  }
}
