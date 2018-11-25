package com.zhiyong.tingxie.ui.word;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;

    private List<WordItem> mWordItems;

    WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
            WordItem current = mWordItems.get(i);
            holder.tvWord.setText(current.getWord());
            holder.tvPinyin.setText(current.getPinyin());
        } else {
            holder.tvWord.setText("No Word");
            holder.tvPinyin.setText("No pinyin");
        }
    }

    void setWordItems(List<WordItem> wordItems) {
        mWordItems = wordItems;
        notifyDataSetChanged();
    }

    public void onItemRemove(final RecyclerView.ViewHolder viewHolder,
                             final RecyclerView mRecyclerView,
                             final WordViewModel wordViewModel) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final WordItem wordItem = mWordItems.get(adapterPosition);

        final QuizPinyin quizPinyin = new QuizPinyin(wordItem.getQuizId(), wordItem.getPinyinId());

        Snackbar snackbar = Snackbar
                .make(mRecyclerView, "Removed word", Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWordItems.add(adapterPosition, wordItem);
                        notifyItemInserted(adapterPosition);
                        wordViewModel.addQuizPinyin(quizPinyin);
                        mRecyclerView.scrollToPosition(adapterPosition);
                    }
                });
        snackbar.show();
        mWordItems.remove(adapterPosition);
        wordViewModel.deleteWord(quizPinyin);
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

        private WordViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvPinyin = itemView.findViewById(R.id.tvPinyin);
            ivPlay = itemView.findViewById(R.id.ivPlay);
        }
    }
}
