package com.example.chatfriend.model

class User(val myUId: String, val userName: String,val profileImageUrl: String) {
    constructor() : this("", "", "")
}
