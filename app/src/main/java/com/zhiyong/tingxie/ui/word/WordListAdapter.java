package com.zhiyong.tingxie.ui.word;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyong.tingxie.R;
import com.zhiyong.tingxie.db.QuizPinyin;

import java.util.List;
import java.util.Locale;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private final TextToSpeech textToSpeech;
    private final Context context;

    private List<WordItem> mWordItems;

    WordListAdapter(final Context context) {
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
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_word, viewGroup, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, final int i) {
        if (mWordItems != null) {
            final WordItem current = mWordItems.get(i);
            final String word = current.getWord();
            holder.tvWord.setText(word);
            holder.tvPinyin.setText(current.getPinyin());
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
        } else {
            holder.tvWord.setText("No Word");
            holder.tvPinyin.setText("No pinyin");
        }
    }

    void setWordItems(List<WordItem> wordItems) {
        mWordItems = wordItems;
        notifyDataSetChanged();
    }

    void onItemRemove(RecyclerView.ViewHolder viewHolder,
                      final RecyclerView mRecyclerView,
                      final WordViewModel viewModel) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final WordItem wordItem = mWordItems.get(adapterPosition);
        final QuizPinyin quizPinyin = new QuizPinyin(wordItem.getQuizId(), wordItem.getPinyin());

        Snackbar snackbar = Snackbar
                .make(mRecyclerView, "Removed word", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWordItems.add(adapterPosition, wordItem);
                        notifyItemInserted(adapterPosition);
                        viewModel.addQuizPinyin(quizPinyin);
                        mRecyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        mWordItems.remove(adapterPosition);
        viewModel.deleteWord(quizPinyin);
        notifyItemRemoved(adapterPosition);
    }

    @Override
    public int getItemCount() {
        if (mWordItems != null)
            return mWordItems.size();
        return 0;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvWord;
        private final TextView tvPinyin;
        private final ImageView ivPlay;
        private final ConstraintLayout wordLayout;

        private WordViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPinyin = itemView.findViewById(R.id.tvPinyin);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            wordLayout = itemView.findViewById(R.id.wordLayout);
        }
    }
}
