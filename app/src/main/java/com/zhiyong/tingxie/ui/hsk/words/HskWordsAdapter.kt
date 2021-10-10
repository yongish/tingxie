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
import androidx.lifecycle.LifecycleOwner
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
          Log.i("numQuizItems", "000000000000000")
          val sdf = SimpleDateFormat("yyyymmdd")
          val currentDate = sdf.format(Date())
          // todo: Specifying totalWords in quiz is a hack.
          // Should first check the return value of insertQuizPinyin, then update the quizItem.
          val quizId = quizViewModel.insertQuizFuture(
            Quiz(Integer.valueOf(currentDate), 1)
          )
          quizViewModel.insertQuizPinyin(
            QuizPinyin(quizId, wordItem.pinyin, word, false)
          )
          Toast.makeText(context, "Added to a new quiz", Toast.LENGTH_SHORT).show()
        }
        1 -> {
          Log.i("numQuizItems", "111111111111111")
        }
        else -> {
          Log.i("numQuizItems", "more")
        }
      }
    }

//      viewModel.allQuizItems.observe(lifecycleOwner, { Log.i("PL", "placeholder") })
//      viewModel.addHskWordToQuiz(mWordItems[position]) {
//        if (it) {
//          Toast.makeText(context, "Added to a new quiz", Toast.LENGTH_SHORT).show()
//        } else {
//          val b: AlertDialog.Builder = AlertDialog.Builder(context)
//          b.setTitle("Select a quiz to add this word to.")
//          val quizItems = viewModel.allQuizItems.value
//          if (quizItems == null) {
//            Log.e("HskWordsAdapter", "quizItems should not be null.")
//          } else {
//            val quizItemStrings: Array<String> = quizItems.map { quizItem -> "${quizItem.date}|${quizItem.title}" }.toTypedArray()
//            b.setItems(quizItemStrings) { dialog, which ->
//              dialog.dismiss()
//              val pair = Pair(
//                quizItemStrings[which].substringBefore('|').trim(),
//                quizItemStrings[which].substringAfter('|').trim()
//              )
//            }
//            b.show()
//          }
//        }
//      }
//      notifyDataSetChanged()
//      val addedNewQuiz = viewModel.addHskWordToQuiz(mWordItems[position])
//      if (addedNewQuiz) {
//        Toast.makeText(context, "Added to a new quiz", Toast.LENGTH_SHORT).show()
//      } else {
//        // todo: stopped here. Next step is to ask the user to select an existing quiz.
//          // Use a spinner dialog.
//        val b: AlertDialog.Builder = AlertDialog.Builder(context)
//        b.setTitle("Select a quiz to add this word to.")
//        val quizItems = viewModel.allQuizItems.value
//        if (quizItems == null) {
//          Log.e("HskWordsAdapter", "quizItems should not be null.")
//        } else {
//          val quizItemStrings: Array<String> = quizItems.map { quizItem -> "${quizItem.date}|${quizItem.title}" }.toTypedArray()
//          b.setItems(quizItemStrings) { dialog, which ->
//            dialog.dismiss()
//            val pair = Pair(
//              quizItemStrings[which].substringBefore('|').trim(),
//              quizItemStrings[which].substringAfter('|').trim()
//            )
//          }
//          b.show()
//        }
//      }
//    }
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