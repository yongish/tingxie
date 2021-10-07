package com.zhiyong.tingxie.ui.hsk.words

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import java.util.*

class HskWordsAdapter(private var context: Context) : RecyclerView.Adapter<HskWordsAdapter.ViewHolder>() {
  private lateinit var mWordItems: List<HskWord>
  private lateinit var textToSpeech: TextToSpeech

  init {
    textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
      if (status != TextToSpeech.ERROR){
        //if there is no error then set language
        textToSpeech.language = Locale.SIMPLIFIED_CHINESE
      }
    })
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(mWordItems[position])
    val word = mWordItems[position].hanzi
    holder.tvHanzi.setOnClickListener {
      context.startActivity(Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://baike.baidu.com/item/$word")
      ))
    }
    holder.ivPlay.setOnClickListener {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        textToSpeech.speak(word,TextToSpeech.QUEUE_FLUSH,null,null);
      } else {
        textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder.from(parent, context)

  override fun getItemCount(): Int {
    if (::mWordItems.isInitialized) {
      return mWordItems.size
    }
    return 0
  }

  fun setWordItems(wordItems: List<HskWord>) {
    mWordItems = wordItems
    notifyDataSetChanged()
  }

  override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    super.onDetachedFromRecyclerView(recyclerView)
    textToSpeech.stop()
    textToSpeech.shutdown()
  }

  data class HskWord(val index: Int, val hanzi: String, val pinyin: String)

  class ViewHolder private constructor(itemView: View, val context: Context)
    : RecyclerView.ViewHolder(itemView) {
    private val tvIndex: TextView = itemView.findViewById(R.id.tvIndex)
    val tvHanzi: TextView = itemView.findViewById(R.id.tvHanzi)
    private val tvPinyin: TextView = itemView.findViewById(R.id.tvPinyin)
    val ivPlay: ImageView = itemView.findViewById(R.id.ivPlay)

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