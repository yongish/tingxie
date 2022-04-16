package com.zhiyong.tingxie.ui.share

enum class EnumQuizRole {
    EDITOR, VIEWER
}

data class TingXieShareIndividual(
    val email: String,
    val name: String,
    val isShared: Boolean,
    val role: EnumQuizRole
    )
