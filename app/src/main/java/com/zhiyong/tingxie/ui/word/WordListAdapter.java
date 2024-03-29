package com.zhiyong.tingxie.ui.word;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.QuizPinyin;
import com.zhiyong.tingxie.network.NetworkQuiz;

import java.util.List;
import java.util.Locale;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private final TextToSpeech textToSpeech;
    private final Context context;

    private List<WordItem> mWordItems;

    private final WordViewModel viewModel;
    private final RecyclerView recyclerView;
    private final NetworkQuiz quizItem;

    WordListAdapter(final Context context, WordViewModel viewModel,
                    RecyclerView recyclerView, NetworkQuiz quizItem) {
        mInflater = LayoutInflater.from(context);
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                }
            }
        });
        this.context = context;
        this.viewModel = viewModel;
        this.recyclerView = recyclerView;
        this.quizItem = quizItem;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_word, viewGroup, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder, final int i) {
        if (mWordItems != null) {
            final WordItem current = mWordItems.get(i);
            final String word = current.getWordString();
            holder.tvWord.setText(word);
            holder.tvPinyin.setText(current.getPinyinString());
            holder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            holder.wordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://baike.baidu.com/item/" + word;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    context.startActivity(intent);
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemRemove(holder);
                }
            });
        } else {
            holder.tvWord.setText(R.string.no_word);
            holder.tvPinyin.setText(R.string.no_pinyin);
        }
    }

    void setWordItems(List<WordItem> wordItems) {
        mWordItems = wordItems;
        notifyDataSetChanged();
    }

    void onItemRemove(RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final WordItem wordItem = mWordItems.get(adapterPosition);

        // todo: 12/1/22 Stubbed out asked with false. To correct this.
        final QuizPinyin quizPinyin = new QuizPinyin(wordItem.getQuizId(),
                wordItem.getPinyinString(), wordItem.getWordString(), false);

        Snackbar snackbar = Snackbar
                .make(recyclerView, "Removed word", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        viewModel.addQuizPinyin(quizPinyin);

                        viewModel.addWord(quizPinyin.getQuizId(), quizPinyin.getWordString(), quizPinyin.getPinyinString());

                        viewModel.updateQuiz(quizItem);
//                        mWordItems.add(adapterPosition, wordItem);
//                        notifyItemInserted(adapterPosition);
                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();

        viewModel.deleteWord(wordItem);
        mWordItems.remove(adapterPosition);

        int totalWords = quizItem.getNumWords() - 1;
        quizItem.setNumWords(totalWords);
        quizItem.setNumNotCorrect(totalWords);
        quizItem.setRound(1);
        viewModel.updateQuiz(quizItem);
//                new QuizItem(
//                quizItem.getId(), quizItem.getDate(), quizItem.getTitle(),
//                quizItem.getNumWords(), quizItem.getNumNotCorrect(), quizItem.getRound()

        notifyItemRemoved(adapterPosition);
    }

    @Override
    public int getItemCount() {
        if (mWordItems != null)
            return mWordItems.size();
        return 0;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvWord;
        private final TextView tvPinyin;
        private final ImageView ivPlay;
        private final ConstraintLayout wordLayout;
        private final ImageView ivDelete;

        private WordViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvHanzi);
            tvPinyin = itemView.findViewById(R.id.tvPinyin);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            wordLayout = itemView.findViewById(R.id.wordLayout);
            ivDelete = itemView.findViewById(R.id.btnMembers);
        }
    }
}
