package com.zhiyong.tingxie.ui.readings

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.databinding.RecyclerviewReadingsBinding
import com.zhiyong.tingxie.network.NetworkTitle
import com.zhiyong.tingxie.ui.reading.ReadingActivity
import java.time.Instant
import java.time.ZoneId

class ReadingsAdapter(
  private val context: Context,
  val viewModel: ReadingsViewModel,
  val recyclerView: RecyclerView
) : RecyclerView.Adapter<ReadingsAdapter.ViewHolder>() {

  companion object {
    const val EXTRA_ID = "com.zhiyong.tingxie.ui.readings.extra.ID"
  }

  var readings = mutableListOf<NetworkTitle>()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(
      RecyclerviewReadingsBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val reading = readings[position]
    holder.bind(reading)

    holder.btnOpen.setOnClickListener {
      val intent = Intent(context, ReadingActivity::class.java)
      intent.putExtra(EXTRA_ID, reading.id)
      context.startActivity(intent)
    }
  }

  override fun getItemCount(): Int = readings.size

  class ViewHolder(private val binding: RecyclerviewReadingsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val btnOpen = binding.btnOpen

    fun bind(title: NetworkTitle) = with(binding) {
      tvTitle.text = title.title
      tvDateOpened.text =
        if (title.timestampOpened == -1) "Unread" else "Opened on " + Instant.ofEpochMilli(
          title.timestampOpened.toLong()
        ).atZone(ZoneId.systemDefault()).toLocalDateTime().toString()
    }
  }
}
