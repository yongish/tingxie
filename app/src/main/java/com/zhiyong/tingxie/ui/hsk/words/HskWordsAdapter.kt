package com.zhiyong.tingxie.ui.hsk.words

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R

class HskWordsAdapter : RecyclerView.Adapter<HskWordsAdapter.ViewHolder>() {
  private lateinit var mWordItems: List<HskWord>

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(mWordItems[position])
    val word = mWordItems[position].hanzi
    holder.tvHanzi.setOnClickListener {
      val webIntent: Intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://baike.baidu.com/item/$word")
      )
      holder.context.startActivity(webIntent)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder.from(parent, parent.context)

  override fun getItemCount(): Int = mWordItems.size

  fun setWordItems(wordItems: List<HskWord>) {
    mWordItems = wordItems
    notifyDataSetChanged()
  }

  data class HskWord(val index: Int, val hanzi: String, val pinyin: String)

  class ViewHolder private constructor(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
    val tvIndex: TextView = itemView.findViewById(R.id.tvIndex)
    val tvHanzi: TextView = itemView.findViewById(R.id.tvHanzi)
    val tvPinyin: TextView = itemView.findViewById(R.id.tvPinyin)
    val context: Context = context

    fun bind(item: HskWord) {
      tvIndex.text = context.getString(R.string.index, item.index)
      tvHanzi.text = item.hanzi
      tvPinyin.text = item.pinyin
    }

    companion object {
      fun from(parent: ViewGroup, context: Context): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recyclerview_hsk_word, parent, false)
        return ViewHolder(view, context)
      }
    }
  }
}