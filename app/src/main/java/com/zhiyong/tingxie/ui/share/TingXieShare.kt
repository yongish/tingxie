package com.zhiyong.tingxie.ui.share

enum class EnumQuizRole {
    EDITOR, VIEWER
}

data class TingXieShare(
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: EnumQuizRole
    )
