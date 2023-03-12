package com.zhiyong.tingxie.ui.share

enum class EnumQuizRole {
    // There can only be 1 owner.
    // If the owner deletes a quiz that has other users, she is presented with a dropdown to choose another
    // owner, If she doesn't choose one, the first entry becomes the owner.

    OWNER, ADMIN, MEMBER
}
