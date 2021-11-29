package com.zhiyong.tingxie.ui.share

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhiyong.tingxie.R
import com.zhiyong.tingxie.ui.share.ShareActivity.Companion.EXTRA_QUIZ_ID

class ShareFragment : Fragment() {

    companion object {
        fun newInstance() = ShareFragment()
    }

    private lateinit var viewModel: ShareViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.share_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quizId = arguments?.getLong(EXTRA_QUIZ_ID)

        viewModel = ViewModelProvider(this).get(ShareViewModel::class.java)

        // todo: Get editors and viewers of quiz.
//        viewModel
    }
}
