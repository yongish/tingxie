package com.zhiyong.tingxie.ui

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.zhiyong.tingxie.ui.login.LoginActivity

open class CommonFragment : Fragment() {
  data class User(val email: String, val displayName: String)

  fun getFirebaseUser(): User {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser == null) {
      startActivity(Intent(context, LoginActivity::class.java))
      return User("", "")
    }
    return User(currentUser.email!!, currentUser.displayName!!)
  }
}