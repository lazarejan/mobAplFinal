package com.example.fbclone.model

import androidx.compose.runtime.mutableStateListOf

data class User(val email: String, val pass: String)

data class Post(
    val id: Int,
    val authorEmail: String,
    val content: String,
    val likedBy: MutableList<String> = mutableStateListOf()
)


object MockDatabase {
    val users = mutableListOf(User("test@test.com", "1234"))
    val posts = mutableStateListOf(
        Post(1, "test@test.com", "Welcome to the local Facebook clone! No internet needed.")
    )
}