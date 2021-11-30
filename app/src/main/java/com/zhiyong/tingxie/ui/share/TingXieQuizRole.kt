package com.zhiyong.tingxie.ui.share

enum class EnumQuizRole {
    EDITOR, VIEWER
}

data class TingXieQuizRole(val quizId: Long, val email: String, val role: EnumQuizRole)
