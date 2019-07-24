package com.example.chatfriends.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatfriend.model.User
import com.example.chatfriends.R

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        //val adapter = GroupAdapter<ViewHolder>()

        //recyclerview_newmessage.adapter = adapter

        fetchUsers()

        //recyclerview_newmessage.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{


            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach{

                    Log.d("NewMessage", it.toString())

                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    startActivity(intent)

                    finish()

                }

                recyclerview_newmessage.adapter = adapter

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //bizim user listemizdeki her obje için çağrılır
        viewHolder.itemView.textView_new_message.text = user.userName

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_mesage)

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}