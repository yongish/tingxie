package com.zhiyong.tingxie.ui.hsk.words

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.db.Quiz
import com.zhiyong.tingxie.db.QuizPinyin
import com.zhiyong.tingxie.ui.main.QuizItem
import com.zhiyong.tingxie.ui.main.QuizViewModel
import com.zhiyong.tingxie.ui.question.QuestionActivity
import java.text.SimpleDateFormat
import java.util.*


class HskWordsAdapter(
  private var context: Context,
  var viewModel: HskWordsViewModel,
  var quizViewModel: QuizViewModel
  ) : RecyclerView.Adapter<HskWordsAdapter.ViewHolder>() {
  private lateinit var mWordItems: List<HskWord>
  private lateinit var quizItems: List<QuizItem>
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
    val wordItem = mWordItems[position]
    val word = wordItem.hanzi
    holder.tvHanzi.setOnClickListener {
      context.startActivity(Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://baike.baidu.com/item/$word")
      ))
    }
    holder.ivPlay.setOnClickListener { QuestionActivity.speak(textToSpeech, word) }
    holder.ibAdd.setOnClickListener {
      when (quizItems.size) {
        0 -> {
          Log.i("numQuizItems", "0")
          val sdf = SimpleDateFormat("yyyymmdd")
          val currentDate = sdf.format(Date())


          // todo FIX THIS!!!
//          val quizId = quizViewModel.insertQuizFuture(
//            Quiz(Integer.valueOf(currentDate))
//          )
//          quizViewModel.insertQuizPinyin(
//            QuizPinyin(quizId, wordItem.pinyin, word, false)
//          )



          Toast.makeText(context, "Added to a new quiz", Toast.LENGTH_SHORT).show()
        }
        1 -> {
          Log.i("numQuizItems", "1")
          quizViewModel.insertQuizPinyin(
            QuizPinyin(quizItems[0].id, wordItem.pinyin, word, false)
          )
          Toast.makeText(context, "Added to your quiz", Toast.LENGTH_SHORT).show()
        }
        else -> {
          Log.i("numQuizItems", ">1")
          val b: AlertDialog.Builder = AlertDialog.Builder(context)
          b.setTitle("Select a quiz to add this word to.")
          val quizItemStrings: Array<String> = quizItems.map {
              quizItem -> "${quizItem.date} | ${quizItem.title}"
          }.toTypedArray()
          b.setItems(quizItemStrings) { dialog, which ->
            dialog.dismiss()
            val updatedQuizItem = quizItems[which]
            quizViewModel.insertQuizPinyin(
              QuizPinyin(updatedQuizItem.id, wordItem.pinyin, word, false)
            )
            quizViewModel.updateQuiz(Quiz(
              updatedQuizItem.id,
              updatedQuizItem.date,
              updatedQuizItem.title,
              updatedQuizItem.totalWords + 1,
              updatedQuizItem.totalWords + 1,
              1
            ))
            Toast.makeText(context, "Added to selected quiz", Toast.LENGTH_SHORT).show()
          }
          b.show()
        }
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(parent, context)
  }

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

  fun setQuizItems(quizItems: List<QuizItem>) {
    this.quizItems = quizItems
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
    val ibAdd: ImageButton = itemView.findViewById(R.id.ibAdd)

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