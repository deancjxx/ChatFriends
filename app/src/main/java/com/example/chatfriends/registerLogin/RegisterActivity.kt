package com.example.chatfriends.registerLogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.chatfriend.model.User
import com.example.chatfriends.R
import com.example.chatfriends.messages.LatestMessagesActivity
import com.example.chatfriends.registerLogin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {


    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    private lateinit var mReference: DatabaseReference
    private lateinit var mUserReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        register_button_register.setOnClickListener {

            //val userName = editText_register_userName.text.toString()
            val email = editText_register_mail.text.toString()
            val password = editText_register_password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){

                if (password.length >= 6){
                    performRegister(email,password)
                }
                else {
                    editText_register_password.error = "Şifre En az 6 Karakter Olmalı!"
                }
            }
            else {
                if (email.isEmpty()) editText_register_mail.error = "Email Boş Geçilemez"
                if (password.isEmpty()) editText_register_password.error = "Şifre Boş Geçilemez"
                //if (userName.isEmpty()) editText_register_userName.error = "Kullanıcı Adı Boş Geçilemez"
            }
        }

        login_button_register.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        button_select_photo.setOnClickListener {
            Log.d("Main" ," Try photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("Resim","Resim Seçildi")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)

            select_photo_imageview_register.setImageBitmap(bitmap)
            button_select_photo.alpha = 0f
            /*val bitmapDrawable = BitmapDrawable(bitmap)
            select_photo_imagewiew_register.setBackgroundDrawable(bitmapDrawable)*/

        }

    }

    private fun performRegister(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Toast.makeText(this,"Başarısız!",Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                else {
                    Log.d("Main", "Kayıt tamam ${it.result?.user?.uid}")
                    /*val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)*/

                    uploadImageToFirebaseStore()
                }

            }
            .addOnFailureListener {
                Log.d("Main","Failed : ${it.message}")
            }

    }

    private fun uploadImageToFirebaseStore() {

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("REgister", "Upload image ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register", "dosya yeri $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {

            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val mUserId = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$mUserId")

        val user = User(mUserId, editText_register_userName.text.toString(),profileImageUrl )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Database kaydedildi")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
    }

}


