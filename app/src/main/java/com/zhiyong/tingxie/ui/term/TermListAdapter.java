package com.zhiyong.tingxie.ui.term;

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
import com.zhiyong.tingxie.db.Term;

import java.util.List;
import java.util.Locale;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private final TextToSpeech textToSpeech;
    private final Context context;

//    private List<WordItem> mWordItems;
    private List<Term> mTerms;

    private TermViewModel viewModel;
    private RecyclerView recyclerView;

    TermListAdapter(final Context context, TermViewModel viewModel, RecyclerView recyclerView) {
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
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mInflater.inflate(R.layout.recyclerview_word, viewGroup, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordViewHolder holder, final int i) {
        if (mTerms != null) {
//            final WordItem current = mWordItems.get(i);
            final Term current = mTerms.get(i);
//            final String word = current.getWord();
            final String word = current.getWord();
            holder.tvWord.setText(word);
//            holder.tvPinyin.setText(current.getPinyin());
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
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemRemove(holder);
                }
            });
        } else {
            holder.tvWord.setText("No Word");
            holder.tvPinyin.setText("No pinyin");
        }
    }

    void setTermItems(List<Term> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

    void onItemRemove(RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
//        final WordItem wordItem = mWordItems.get(adapterPosition);
        final Term term = mTerms.get(adapterPosition);
//        final QuizPinyin quizPinyin = new QuizPinyin(wordItem.getQuizId(), wordItem.getPinyin());

        Snackbar snackbar = Snackbar
                .make(recyclerView, "Removed word", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mWordItems.add(adapterPosition, wordItem);
                        mTerms.add(adapterPosition, term);
                        notifyItemInserted(adapterPosition);
                        viewModel.addTerm(term);
//                        viewModel.addQuizPinyin(quizPinyin);
                        recyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        mTerms.remove(adapterPosition);
//        mWordItems.remove(adapterPosition);
//        viewModel.deleteWord(quizPinyin);
        viewModel.deleteTerm(term);
        notifyItemRemoved(adapterPosition);
    }

    @Override
    public int getItemCount() {
        if (mTerms != null) return mTerms.size();
//        if (mWordItems != null)
//            return mWordItems.size();
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
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPinyin = itemView.findViewById(R.id.tvPinyin);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            wordLayout = itemView.findViewById(R.id.wordLayout);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
