package com.example.fbclone.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fbclone.model.MockDatabase
import com.example.fbclone.model.Post

class PostViewModel : ViewModel() {
    val posts = MockDatabase.posts

    fun createPost(content: String, authorEmail: String, onSuccess: () -> Unit) {
        val newId = (posts.maxOfOrNull { it.id } ?: 0) + 1
        val newPost = Post(id = newId, authorEmail = authorEmail, content = content)
        posts.add(0, newPost)
        onSuccess()
    }

    fun toggleLike(post: Post, userEmail: String) {
        if (post.likedBy.contains(userEmail)) {
            post.likedBy.remove(userEmail)
        } else {
            post.likedBy.add(userEmail)
        }
    }
}