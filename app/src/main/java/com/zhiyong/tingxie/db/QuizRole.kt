package com.zhiyong.tingxie.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.zhiyong.tingxie.ui.share.EnumQuizRole

@Entity
@TypeConverters(QuizRoleConverter::class)
data class QuizRole(
  @NonNull val quizId: Long,
  @NonNull val email: String,
  @NonNull val role: EnumQuizRole
) {
  @PrimaryKey(autoGenerate = true) var id: Long = 0
}

class QuizRoleConverter {
    @TypeConverter
    fun toRole(value: String) = enumValueOf<EnumQuizRole>(value)

    @TypeConverter
    fun fromRole(value: EnumQuizRole) = value.name
}
