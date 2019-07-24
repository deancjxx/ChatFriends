package com.example.chatfriends.registerLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatfriends.R
import com.example.chatfriends.registerLogin.RegisterActivity
import com.example.chatfriends.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


val mAuth = FirebaseAuth.getInstance()

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            loginChat()
        }

        register_button_login.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginChat() {

        val email = editText_login_mail.text.toString()
        val password = editText_login_password.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (!it.isSuccessful) {
                    return@addOnCompleteListener
                } else {

                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}
