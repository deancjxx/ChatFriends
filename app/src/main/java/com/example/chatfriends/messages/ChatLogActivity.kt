package com.example.chatfriends.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatfriends.R

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"
    }
}
